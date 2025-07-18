from fastapi import FastAPI, File, UploadFile
import whisper
import os
import uuid
import logging
import subprocess
import tempfile

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

MODEL_NAME = os.getenv("WHISPER_MODEL", "base")
logger.info(f"Загрузка модели Whisper: {MODEL_NAME}")
model = whisper.load_model(MODEL_NAME)

app = FastAPI(title="Whisper ASR API", version="1.0")

@app.post("/transcribe")
async def transcribe_audio(file: UploadFile = File(...)):
    try:
        file_id = str(uuid.uuid4())
        tmp_dir = tempfile.gettempdir()
        input_path = os.path.join(tmp_dir, f"{file_id}.ogg")
        output_path = os.path.join(tmp_dir, f"{file_id}_converted.wav")

        data = await file.read()
        logger.info(f"Размер загруженного файла: {len(data)} байт")

        with open(input_path, "wb") as f:
            f.write(data)

        subprocess.run(["ffmpeg", "-y", "-i", input_path, "-ac", "1", "-ar", "16000", output_path], check=True)

        result = model.transcribe(output_path)
        text = result["text"]

        os.remove(input_path)
        if os.path.exists(output_path):
            os.remove(output_path)

        return {"text": text}

    except Exception as e:
        logger.error(f"Ошибка при транскрипции: {e}")
        return {"error": str(e)}
