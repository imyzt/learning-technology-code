FROM python:3.11-slim

WORKDIR /app

RUN pip install --no-cache-dir \
    flask==3.0.0 \
    flask-cors==4.0.0 \
    psycopg2-binary==2.9.9 \
    requests==2.31.0 \
    python-dotenv==1.0.0

COPY app.py .

EXPOSE 8888

CMD ["python", "app.py"]
