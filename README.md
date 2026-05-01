# Meeting Assistant - Android App

An AI-powered Android meeting assistant built with **Kotlin + Jetpack Compose** that uses voice commands, real-time transcription, and OpenAI's LLM to help you capture, understand, and act on your meetings.

## Features

### Voice Input & Transcription
- **Real-time speech-to-text** using Android's SpeechRecognizer
- **Continuous listening mode** during meetings with auto-restart
- **Voice commands** to interact with the AI assistant hands-free

### AI-Powered Intelligence (OpenAI)
- **Smart Q&A** - Ask questions about your meeting in natural language
- **Auto-summarization** - Generate meeting summaries with one tap
- **Action item extraction** - Automatically identify tasks and follow-ups
- **Context-aware responses** - AI understands the full meeting context

### Audio Responses (Text-to-Speech)
- **AI reads responses aloud** using Android's TextToSpeech engine
- **Adjustable speed & pitch** for comfortable listening
- **Auto-speak mode** - Responses are read automatically

### Meeting Management
- **Start/stop meetings** with a live timer
- **Browse past meetings** with transcripts, summaries, and action items
- **Export/share meetings** via Android share sheet
- **Local persistence** using SharedPreferences

---

## Project Structure

```
MeetingAssistantAndroid/
├── build.gradle.kts                    # Root build config
├── settings.gradle.kts                 # Project settings
├── gradle.properties                   # Gradle properties
├── gradle/wrapper/
│   └── gradle-wrapper.properties       # Gradle wrapper config
└── app/
    ├── build.gradle.kts                # App dependencies & config
    ├── proguard-rules.pro              # ProGuard rules
    └── src/main/
        ├── AndroidManifest.xml         # Permissions & app config
        ├── res/
        │   └── values/
        │       ├── strings.xml
        │       ├── colors.xml
        │       └── themes.xml
        └── java/com/meetingassistant/app/
            ├── MainActivity.kt              # Entry point
            ├── MeetingAssistantApp.kt       # Application class
            ├── data/
            │   ├── models/
            │   │   ├── Meeting.kt           # Meeting data model
            │   │   ├── Message.kt           # Chat message model
            │   │   └── TranscriptEntry.kt   # Transcript entry model
            │   └── repository/
            │       └── MeetingRepository.kt # Data persistence
            ├── services/
            │   ├── SpeechRecognitionService.kt  # Voice-to-text
            │   ├── TextToSpeechService.kt       # Text-to-voice
            │   └── LLMService.kt                # OpenAI API
            ├── viewmodels/
            │   ├── MeetingViewModel.kt      # Meeting screen logic
            │   ├── ChatViewModel.kt         # AI chat logic
            │   └── SettingsViewModel.kt     # Settings logic
            └── ui/
                ├── theme/
                │   ├── Color.kt             # Color palette
                │   ├── Type.kt              # Typography
                │   └── Theme.kt             # Material 3 theme
                ├── navigation/
                │   └── NavGraph.kt          # Navigation routes
                ├── screens/
                │   ├── HomeScreen.kt        # Dashboard
                │   ├── MeetingScreen.kt     # Active meeting
                │   ├── ChatScreen.kt        # AI chat
                │   ├── TranscriptScreen.kt  # Past meeting review
                │   └── SettingsScreen.kt    # App settings
                └── components/
                    ├── VoiceButton.kt       # Animated mic button
                    ├── MessageBubble.kt     # Chat bubble
                    └── MeetingCard.kt       # Meeting list card
```

---

## Setup Instructions (Windows PC)

### Prerequisites
- **Windows 10/11** PC
- **Android Studio Hedgehog (2023.1.1)** or newer
- **Android phone** with USB cable (or use the emulator)
- **OpenAI API key** from [platform.openai.com](https://platform.openai.com)

### Step 1: Install Android Studio

1. Download Android Studio from [developer.android.com/studio](https://developer.android.com/studio)
2. Run the installer and follow the setup wizard
3. During setup, install the **Android SDK** (API 34) when prompted
4. Wait for all components to download and install

### Step 2: Open the Project

1. Open **Android Studio**
2. Click **"Open"** (not "New Project")
3. Navigate to `C:\Users\Shaik_Muneer\MeetingAssistantAndroid`
4. Select the folder and click **OK**
5. Wait for Gradle to sync (this may take a few minutes the first time)
6. If prompted to update Gradle or AGP, click **"Update"**

### Step 3: Connect Your Android Phone

1. On your phone, go to **Settings > About Phone**
2. Tap **"Build Number"** 7 times to enable Developer Options
3. Go to **Settings > Developer Options**
4. Enable **"USB Debugging"**
5. Connect your phone via USB cable
6. Tap **"Allow"** on the USB debugging prompt on your phone
7. In Android Studio, your phone should appear in the device dropdown (top toolbar)

### Step 4: Build and Run

1. Select your phone from the device dropdown
2. Click the **green Play button** (or press Shift+F10)
3. Wait for the build to complete (first build takes longer)
4. The app will install and launch on your phone!
5. Grant **Microphone** permission when prompted

### Step 5: Configure the App

1. Open the app on your phone
2. Tap **"Settings"** (gear icon)
3. Enter your **OpenAI API key** (starts with `sk-`)
4. Choose your preferred AI model (gpt-4o-mini recommended)
5. Configure voice settings if desired
6. Tap **"Save Settings"**

---

## How to Use

### Starting a Meeting
1. Tap **"New Meeting"** on the home screen
2. Enter a meeting title (or use the auto-generated one)
3. Tap **"Start Meeting"**
4. The app begins recording and transcribing automatically

### During a Meeting
- **Red mic button**: Tap to pause/resume recording
- **Summary button**: Generate an AI summary at any time
- **Menu (...)**: Access AI chat, generate summary, or end meeting
- The transcript updates in real-time as you speak

### Asking the AI
1. Tap **"Ask AI"** from the home screen or meeting menu
2. Type a question or tap the **mic button** for voice input
3. The AI responds with context from your current meeting
4. Tap the **speaker icon** on any response to hear it read aloud

### Suggested Questions
- "Summarize the key points discussed"
- "What action items were mentioned?"
- "What decisions were made?"
- "Who needs to follow up on what?"

### After a Meeting
1. Tap **"End"** to stop recording
2. View past meetings on the home screen
3. Tap any meeting to see its transcript, summary, and action items
4. Use the **share button** to export the meeting notes

---

## Configuration Options

| Setting | Description | Default |
|---------|-------------|---------|
| API Key | Your OpenAI API key | Required |
| AI Model | GPT model to use | gpt-4o-mini |
| Auto-Speak | Read AI responses aloud automatically | On |
| Speech Rate | How fast the voice speaks (0.5 - 2.0) | 1.0 |
| Speech Pitch | Voice pitch (0.5 - 2.0) | 1.0 |

---

## Tech Stack

| Technology | Purpose |
|-----------|---------|
| **Kotlin** | Programming language |
| **Jetpack Compose** | Declarative UI framework |
| **Material 3** | Design system with dynamic colors |
| **Navigation Compose** | Screen navigation |
| **ViewModel** | State management |
| **StateFlow** | Reactive state |
| **Coroutines** | Async operations |
| **OkHttp** | HTTP client for API calls |
| **Gson** | JSON serialization |
| **SpeechRecognizer** | Android speech-to-text |
| **TextToSpeech** | Android text-to-speech |
| **SharedPreferences** | Local data persistence |

---

## Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture:

- **Models** (`data/models/`): Pure data classes
- **Repository** (`data/repository/`): Data persistence and management
- **Services** (`services/`): External integrations (Speech, TTS, OpenAI)
- **ViewModels** (`viewmodels/`): Business logic and UI state
- **Views** (`ui/screens/`, `ui/components/`): Composable UI

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Gradle sync fails | File > Invalidate Caches and Restart |
| "No API key configured" | Go to Settings and enter your OpenAI API key |
| Speech recognition not working | Ensure microphone permission is granted; use a physical device |
| Build errors | Ensure Android SDK 34 is installed (Tools > SDK Manager) |
| Phone not detected | Enable USB Debugging; try a different USB cable |
| Slow first build | Normal - subsequent builds are much faster |

---

## Using the Emulator (No Physical Phone)

If you don't have an Android phone:
1. In Android Studio, go to **Tools > Device Manager**
2. Click **"Create Virtual Device"**
3. Select **Pixel 7** (or any phone)
4. Download **API 34** system image
5. Click **Finish** and launch the emulator
6. Note: Speech recognition has limited support on emulators

---

## License

This project is for personal/educational use. Ensure compliance with OpenAI's usage policies when using their API.
