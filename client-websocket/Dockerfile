FROM python:3.12-slim

WORKDIR /app

# Melhora cache: instala deps antes de copiar o resto
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

# Se seu entrypoint for esse arquivo:
CMD ["python", "main.py"]
