package main

import (
	"encoding/json"
	"fmt"
	"github.com/gorilla/mux"
	"github.com/justinas/alice"
	"gopkg.in/validator.v2"
	"log"
	"net/http"
)

type App struct {
	Router     *mux.Router
	Middleware *Middleware
	config     *Env
}

type shortenReq struct {
	URL                 string `json:"url" validate:"nonzero"`
	ExpirationInMinutes int64  `json:"expiration_in_minutes" validate:"min=0"`
}

type shortLinkResp struct {
	ShortLink string `json:"short_link"`
}

func (a *App) Initialize(env *Env) {
	log.SetFlags(log.LstdFlags | log.Lshortfile)
	a.Router = mux.NewRouter()
	a.Middleware = &Middleware{}
	a.config = env
	a.initializeRoutes()
}

func (a *App) initializeRoutes() {
	// 将多个middleware组装到一起
	m := alice.New(a.Middleware.LoggingHandler, a.Middleware.RecoverHandler)

	a.Router.Handle("/api/shorten", m.ThenFunc(a.createShortLink)).Methods("POST")
	a.Router.Handle("/api/info", m.ThenFunc(a.getShortLinkInfo)).Methods("GET")
	a.Router.Handle("/{short_link:[a-zA-Z0-9]{1,11}}", m.ThenFunc(a.redirect)).Methods("GET")
}

func (a *App) createShortLink(writer http.ResponseWriter, request *http.Request) {
	var req shortenReq
	if err := json.NewDecoder(request.Body).Decode(&req); err != nil {
		respondWithError(writer, StatusError{http.StatusBadRequest,
			fmt.Errorf("parse parameters failed %v", request.Body)})
		return
	}
	if err := validator.Validate(req); err != nil {
		respondWithError(writer, StatusError{http.StatusBadRequest,
			fmt.Errorf("validate parameters failed %v", req)})
		return
	}
	defer request.Body.Close()

	s, err := a.config.S.Shorten(req.URL, req.ExpirationInMinutes)
	if err != nil {
		respondWithError(writer, err)
	} else {
		respondWithJSON(writer, http.StatusCreated, shortLinkResp{ShortLink: s})
	}
}

func (a *App) getShortLinkInfo(writer http.ResponseWriter, request *http.Request) {
	val := request.URL.Query()
	s := val.Get("short_link")

	d, err := a.config.S.ShortLinkInfo(s)
	if err != nil {
		respondWithError(writer, err)
	} else {
		respondWithJSON(writer, http.StatusOK, d)
	}
}

func (a *App) redirect(writer http.ResponseWriter, request *http.Request) {
	vars := mux.Vars(request)

	u, err := a.config.S.UnShorten(vars["short_link"])
	if err != nil {
		respondWithError(writer, err)
	} else {
		http.Redirect(writer, request, u, http.StatusTemporaryRedirect)
	}
}

func (a App) Run(add string) {
	log.Fatal(http.ListenAndServe(add, a.Router))
}

func respondWithError(writer http.ResponseWriter, err error) {
	switch e := err.(type) {
	case Error:
		log.Printf("HTTP %d - %s", e.Status(), e)
		respondWithJSON(writer, e.Status(), e.Error())
	default:
		respondWithJSON(writer, http.StatusInternalServerError, http.StatusText(http.StatusInternalServerError))
	}
}

func respondWithJSON(writer http.ResponseWriter, code int, payload interface{}) {

	resp, _ := json.Marshal(payload)

	writer.Header().Set("Content-Type", "application/json")
	writer.WriteHeader(code)
	_, _ = writer.Write(resp)
}
