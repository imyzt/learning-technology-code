package main

import (
	"fmt"
	"github.com/charmbracelet/bubbletea"
	"github.com/charmbracelet/lipgloss"
	"os"
	"strings"
)

var (
	cyan  = lipgloss.NewStyle().Foreground(lipgloss.Color("#00FFFF"))
	green = lipgloss.NewStyle().Foreground(lipgloss.Color("#32CD32"))
	gray  = lipgloss.NewStyle().Foreground(lipgloss.Color("#696969"))
	gold  = lipgloss.NewStyle().Foreground(lipgloss.Color("#B8860B"))
)

var initModel = model{
	todos:    []string{"a", "b", "c"},
	selected: make(map[int]struct{}),
}

func main() {
	cmd := tea.NewProgram(initModel)
	if err := cmd.Start(); err != nil {
		fmt.Println("start fail: ", err)
		os.Exit(1)
	}
}

func (m model) Init() tea.Cmd {
	return nil
}

func (m model) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	switch msg := msg.(type) {
	case tea.KeyMsg:
		switch msg.String() {
		case "ctrl+c", "q":
			return m, tea.Quit
		case "up", "k":
			if m.cursor > 0 {
				m.cursor--
			} else if m.cursor == 0 {
				m.cursor = len(m.todos) - 1
			}
		case "down", "j":
			if m.cursor < len(m.todos)-1 {
				m.cursor++
			} else if m.cursor == len(m.todos)-1 {
				m.cursor = 0
			}
		case "enter", " ":
			if _, ok := m.selected[m.cursor]; ok {
				delete(m.selected, m.cursor)
			} else {
				m.selected[m.cursor] = struct{}{}
			}
		}
	}
	return m, nil
}

func (m model) View() string {
	s := "todo list: \n\n"

	for i, choice := range m.todos {
		cursor := " "
		if m.cursor == i {
			cursor = ">"
		}

		checked := " "
		if _, ok := m.selected[i]; ok {
			checked = "x"
		}

		sprintf := fmt.Sprintf("%s [%s] %s\n", cursor, checked, choice)
		if m.cursor == i {
			s += strings.Trim(green.Render(sprintf), " ")
		} else {
			s += sprintf
		}
	}

	s += "\n Press q to quit. \n"
	return s
}

type model struct {
	todos    []string
	cursor   int
	selected map[int]struct{}
}
