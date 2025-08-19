#!/bin/sh
# Start ollama server in background
ollama serve &
sleep 3

# Tải xuống gemma:2b, bỏ qua lỗi nếu có
ollama pull gemma:2b || true
sleep 5

# Tải xuống nomic-embed-text
ollama pull nomic-embed-text

# Chạy gemma:2b
ollama run gemma:2b