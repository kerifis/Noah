# TalkTV — AAC Remote Communication Tool for Android TV

**TalkTV** is an Augmentative and Alternative Communication (AAC) tool designed for a young autistic child. It directly addresses compulsive TV-remote channel surfing by repurposing the child's existing motor pattern (navigating with the remote D-pad) into intentional, rewarded communication.

---

## 🌟 Core Behavioral & Clinical Principles

1. **Same Motor Pattern, New Outcome**: The child already knows how to press D-pad directional arrows to browse channels. TalkTV channels that exact physical behavior instead of introducing an unfamiliar input device.
2. **Reward = The Desired Item**: Selecting a tile does not merely speak the word aloud — it instantly launches the requested show or video clip. Intentional communication becomes the fastest path to gratification.
3. **Extinction of Random Channel Surfing**: Random or idle remote input yields no reinforcement or flashy feedback from the app. Only intentional tile selection activates voice and content rewards.
4. **Core + Fringe Vocabulary (Staged)**:
   - **Core Words Row** (Top, Yellow/Green Fitzgerald AAC color scheme): High-frequency functional words (*I, want, more, stop, again, like, no, help, feel*) that are permanently pinned.
   - **Category & Show Row** (Below, Orange/Purple): Content categories (*Cartoons, Food Shows, Music, Favorites, People, Places, Describe*) that expand into specific show thumbnails.
5. **ARASAAC Pictograms & Visual Transfer**: Uses high-contrast, thick-outlined vector pictograms consistent with physical AAC boards (e.g. WordPower), enabling bidirectional skill transfer.
6. **Caregiver Accountability & Data Logging**: Every remote tile press is timestamped and stored in a local Room database with phrase context to measure vocabulary growth and evaluate automatic stage progression.

---

## 📱 Vocabulary Progression Table

| Stage | Unlocked Vocabulary | Clinical / Communication Goal |
|---|---|---|
| **Stage 1** | `want`, `stop` + 3–4 Favorite Shows (*Bluey, CoComelon, Cooking, Nursery Songs*) | Single-tile request $\rightarrow$ Immediate rewarded content playback |
| **Stage 2** | Adds `I`, `more`, `again` + Trains show | Two-tile phrases (*"I want"*, *"want more"*) |
| **Stage 3** | Adds `like`, `no`, `help`, `feel` | Requesting + emotional expression and self-advocacy |
| **Stage 4** | Adds People (*Mom, Dad*), Places (*Home, Park*), Describe (*Happy, Fun*) | Full WordPower-style AAC communication board |

---

## 🏗️ Technical Architecture

- **Platform**: Android TV & Google TV (API 26+)
- **Language**: Kotlin 1.9.23
- **UI Framework**: Jetpack Compose for TV (`androidx.tv:tv-material:1.0.0` & `androidx.tv:tv-foundation`)
- **Speech Engine**: Offline Android `TextToSpeech` (`android.speech.tts.TextToSpeech`) with low latency pre-warming
- **Persistence**: Room Database 2.6.1 (`tiles`, `press_events`, `stages`, `app_settings`)
- **Content Launch**: Android TV Intents (`ACTION_VIEW` deep-links for YouTube Kids / TV apps) with seamless internal fallback to **Media3 ExoPlayer 1.3.1**
- **Remote D-Pad Navigation**: High-contrast animated glowing focus borders (`tvFocusGlow`) and subtle scale animation (1.08x) visible from 10 feet away.

---

## 📂 Project Structure

```
NOAH/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── res/
│           │   ├── drawable/               # ARASAAC-style vector drawables & TV banner
│           │   │   ├── ic_talktv_banner.xml
│           │   │   ├── ic_talktv_logo.xml
│           │   │   ├── ic_tile_i.xml
│           │   │   ├── ic_tile_want.xml
│           │   │   ├── ic_tile_more.xml
│           │   │   ├── ic_tile_stop.xml
│           │   │   ├── ic_tile_again.xml
│           │   │   ├── ic_tile_like.xml
│           │   │   ├── ic_tile_no.xml
│           │   │   ├── ic_tile_help.xml
│           │   │   ├── ic_tile_feel.xml
│           │   │   ├── ic_cat_cartoons.xml
│           │   │   ├── ic_cat_food.xml
│           │   │   ├── ic_cat_music.xml
│           │   │   ├── ic_cat_favorites.xml
│           │   │   ├── ic_show_bluey.xml
│           │   │   ├── ic_show_cocomelon.xml
│           │   │   ├── ic_show_cooking.xml
│           │   │   ├── ic_show_nursery.xml
│           │   │   └── ...
│           │   └── values/
│           │       ├── colors.xml          # Fitzgerald Key palette
│           │       ├── strings.xml
│           │       └── themes.xml          # Leanback fullscreen theme
│           └── java/com/talktv/aac/
│               ├── TalkTvApplication.kt
│               ├── MainActivity.kt         # TV launcher activity with child safety back-guard
│               ├── data/
│               │   ├── model/              # Tile, PressEvent, Stage, AppSettings
│               │   ├── local/              # Room DAOs, Converters, Database, InitialSeeder
│               │   └── repository/         # TalkTvRepository with auto-stage progression
│               ├── tts/
│               │   └── TtsManager.kt       # Offline speech synthesis
│               ├── player/
│               │   ├── ContentLauncher.kt   # TV Intent dispatcher
│               │   └── VideoPlayerActivity.kt # Media3 ExoPlayer fullscreen reward player
│               ├── ui/
│               │   ├── theme/              # Color, Type, Theme tokens
│               │   ├── components/         # TileCard (180dp+), PhraseBar, GlowBorder, PinPad
│               │   ├── home/               # HomeScreen & HomeViewModel
│               │   ├── category/           # CategorySubGridScreen
│               │   ├── settings/           # SettingsScreen, Analytics, StageManagement
│               │   └── navigation/         # TalkTvNavHost & Screen routes
├── preview/
│   └── talktv_simulator.html               # Live interactive TV simulator companion
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── gradlew.bat / gradlew
```

---

## 🎮 TV Remote Navigation Guide

| Remote Key | Action in TalkTV |
|---|---|
| **D-pad Arrow Keys** | Traverses across tiles with high-contrast cyan/yellow glow halo |
| **D-pad Center / OK** | Selects tile $\rightarrow$ Speaks word $\rightarrow$ Appends to Phrase Bar |
| **Reward Tile Press** | Speaks full accumulated phrase $\rightarrow$ Launches content show immediately |
| **Remote Back Button** | Returns from subgrid/settings to Home Grid. If a phrase is active on Home, clears phrase first to prevent accidental exit |
| **Idle Timeout (10s)**| Resets phrase bar quietly with no punishment if abandoned |

---

## 🔐 Caregiver & Therapist Portal

1. Navigate to the top-right gear icon on the Phrase Bar and press **OK**.
2. Enter the 4-digit PIN using the on-screen TV remote keypad (**Default PIN: `1234`**).
3. **Capabilities**:
   - **Vocabulary Stages**: Manually switch active stage (1 to 4) or enable **Automatic Progression** based on successful reward count.
   - **Usage Analytics**: Real-time KPI counters (Total Presses, Rewarded Count, Intentional Ratio) and most/least used vocabulary analysis.
   - **Speech & Timer**: Calibrate TTS speech rate, pitch, and idle phrase reset duration.
   - **Tile Manager**: Inspect active tiles and custom deep-links.

---

## 📥 Hosted Download & Installation

- **Release Page**: [kerifis/Noah Releases (v1.2)](https://github.com/kerifis/Noah/releases/tag/v1.2)
- **Direct APK Download**: [TalkTV-v1.2.apk](https://github.com/kerifis/Noah/releases/download/v1.2/TalkTV-v1.2.apk)

```powershell
# Install downloaded APK directly to Android TV via ADB:
adb install -r TalkTV-v1.2.apk

# Or install from local USB directory:
adb install USB-INSTALL/TalkTV-USB-Install.apk
```

---

## 🚀 Building from Source on Android TV

### Prerequisites
- Android Studio Iguana / Jellyfish (or newer)
- Android SDK 34 (Android TV System Image)
- Java 17

### Command-Line Build
```powershell
# In the project directory:
./gradlew assembleDebug

# Install on connected Android TV device or emulator via ADB:
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 🌐 Web Companion Dashboard (Caregiver & SLP Hub)

TalkTV includes a real-time web companion dashboard for parents, therapists, and educators to monitor and control the TV experience from a laptop, tablet, or phone.

### Running the Dashboard Server
```powershell
# From the project directory:
node dashboard/server.js
```
- **Local Dashboard**: [http://localhost:3000](http://localhost:3000)
- **Local Network (Phone / Tablet)**: `http://<your-lan-ip>:3000`
- **Connected TV Simulator**: [http://localhost:3000/preview/talktv_simulator.html](http://localhost:3000/preview/talktv_simulator.html)

### Modules Included
1. **Live Monitor & Aided Modeling**: Real-time mirroring of child tile selections on TV, instant closed-caption subtitle preview, live video player feed, and remote caregiver modeling buttons.
2. **Clinical SLP Analytics**: Intentional vs. random press tracking, extinction curves, vocabulary growth metrics, core vs. fringe usage breakdown, and CSV report export.
3. **Stage Curriculum Roadmap**: Visual 4-stage progression (Single Content Tiles -> Dual Choice -> Multi-Category -> Sentences) with automated progression gates based on intentionality rates.
4. **Tile Studio & Curator**: Custom video clip and AAC tile builder with YouTube Kids URL support, Fitzgerald key categorization, and ARASAAC visual symbols.
5. **Alberta IPP & Behaviour Support Plan (Template F)**: Full student profile for Noah Tuyishime (EPSB Lymburn School 2025–2026), interoception de-escalation protocols (Level 1/2), and **mandatory 6-month review update log** (Baseline, Oct 31, 2025, Jan 30, 2026, and upcoming Jun 23, 2026 reviews).
6. **IEP / SLP Clinical Summary Report**: One-click printable report formatted to Alberta Education standards for team meetings with OT, SLP, and classroom resource teams.

---

## 🤖 Assisted-AI Subtitle Phrase Predictor
When the child uses the TV remote to select AAC tiles, the on-device AI expands telegraphic inputs into natural, grammatically rich closed-caption subtitles directly on the screen:
- `"I want" + "Pancakes"` ➡️ `[AI SUBTITLE] "I would like to watch pancake cooking videos, please."`
- `"Bathroom"` ➡️ `[AI SUBTITLE] "I need to use the washroom right away."`
- `"All done"` ➡️ `[AI SUBTITLE] "I am all done and ready for a break."`

---

## 📋 Alberta IPP 6-Month Review Cycle
- **Review Schedule**: Baseline (Oct 29, 2025) ➔ Review 1 (Oct 31, 2025) ➔ Mid-Year Review 2 (Jan 30, 2026) ➔ Year-End Summary (Jun 23, 2026).
- **Interoceptive De-escalation**: Directly addresses Noah's frustration triggers (hunger, thirst, sensory overload, needing the washroom) before dysregulation occurs.
- **Data-Driven Updates**: Every 6 months, caregivers and teachers log objective mastery stats, review dates, and new IEP goals directly in the dashboard or Android TV app.
