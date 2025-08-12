# LifePilot 🚀

LifePilot is a Telegram bot (and future mobile app) designed to help you track and improve your life’s key metrics:

- Physical state 💪  
- Mental state 🧠  
- Social environment 🌐  
- Goals and actions 🎯  

It checks in twice daily to ask how you’re doing, analyzes your responses using a powerful model (Qwen3), and tracks changes over time. You can chat with LifePilot naturally — via text or voice messages (with Whisper API support) — and receive thoughtful insights tailored to your well-being.

---

Features ✨

- Daily check-ins: Automated push messages twice a day prompt you to reflect on your current state.  
- Voice message support 🎙️: Send voice updates using Whisper speech-to-text integration.  
- Smart metrics analysis 📊: Responses are processed to estimate your physical, mental, and social well-being, plus progress toward your goals.  
- Conversational companion 💬: Chat freely with LifePilot for support and advice.

---

Technical Details 🛠️

- Telegram bot backend written in Java using Spring Framework.  
- PostgreSQL database for storing user data and metrics.  
- Whisper speech-to-text API served by FastAPI (Python) in a separate container.  
- All services run inside Docker containers orchestrated via docker-compose.  
- Hosted on a rented VPS server.

---

Getting Started 🚀

1. Clone the repository.  
2. Copy example environment files:

    ```
    cp ./backend/env/.env.bot.example ./backend/env/.env.bot  
    cp ./backend/env/.env.server.example ./backend/env/.env.server  
    cp ./backend/env/.env.postgres.example ./backend/env/.env.postgres
    ```

3. Edit the copied `.env` files to add required tokens, passwords, and other sensitive information:  

    - `.env.bot` — add your Telegram bot token  
    - `.env.server` — add any server-related secrets or API keys  
    - `.env.postgres` — add your PostgreSQL username, password, and connection details  

4. Run the project using docker-compose:

    ```
    docker-compose up -d
    ```

---

Future Plans 🔮

- Mobile app built with Flutter.  
- Integration with smart devices and fitness trackers.  
- Reminders and progress tracking for life metrics.  
- Memory and learning from user data for better personalization.

---

Contributing 🤝

LifePilot is currently fully open-source. Contributions and feedback are welcome!

---

Contact 📩

For questions or suggestions, please open an issue or reach out directly.

---

Thank you for trying LifePilot — your companion on the journey to better living. 🌟


