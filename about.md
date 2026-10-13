# About TalkTV

## What Is TalkTV?

**TalkTV** is an Augmentative and Alternative Communication (AAC) application built for Android TV. It was created for **Noah**, a young autistic child whose compulsive TV-remote channel surfing has been transformed from a behavioral concern into a pathway for intentional communication.

Instead of introducing an unfamiliar input device, TalkTV hijacks the exact motor pattern Noah already knows — pressing D-pad arrows on a TV remote — and redirects it toward purposeful vocabulary selection, speech synthesis, and rewarded video content playback.

> **In short:** Every intentional tile press speaks a word, builds a phrase, and launches the show Noah asked for. Random or idle button mashing yields nothing. Communication becomes the fastest route to gratification.

---

## Who Is This For?

| Audience | How They Use TalkTV |
|---|---|
| **Noah** | Navigates AAC tiles with the TV remote to request shows, express feelings, and communicate needs |
| **Parents & Caregivers** | Monitor usage via the web dashboard, adjust vocabulary stages, and review progress analytics |
| **Speech-Language Pathologists (SLPs)** | Track intentional vs. random press ratios, export clinical reports, and manage IPP review cycles |
| **Educators (EPSB / Alberta Ed.)** | Access Alberta IPP templates, behaviour support plans, and IEP summaries directly from the dashboard |

---

## Core Design Principles

1. **Same Motor Pattern, New Outcome** — D-pad navigation is repurposed, not replaced. The child's existing remote-control muscle memory is channeled into communication.
2. **Reward = The Desired Item** — Selecting a tile doesn't just speak a word; it immediately launches the requested show or video clip.
3. **Extinction of Random Channel Surfing** — No reinforcement for aimless button presses. Only intentional selections produce voice output and content.
4. **Fitzgerald Key Color Coding** — Vocabulary tiles follow the clinical Fitzgerald color scheme (Yellow/Green for core words, Orange/Purple for categories) ensuring consistency with physical AAC boards.
5. **ARASAAC Pictograms** — High-contrast, thick-outlined vector pictograms enable visual transfer between the TV app and paper/physical AAC systems like WordPower.

---

## Vocabulary Stages

TalkTV uses a staged vocabulary progression model aligned with clinical AAC best practices:

| Stage | Vocabulary Unlocked | Communication Goal |
|---|---|---|
| **1 — Single Tile** | `want`, `stop` + 3–4 favorite shows | Single-tile request → immediate content playback |
| **2 — Two-Tile Phrases** | Adds `I`, `more`, `again` | Two-word phrases like *"I want"*, *"want more"* |
| **3 — Emotion & Self-Advocacy** | Adds `like`, `no`, `help`, `feel` | Requesting + emotional expression |
| **4 — Full Communication Board** | Adds People, Places, Describe categories | Full WordPower-style AAC board with sentences |

Progression can be **automatic** (triggered by successful reward counts) or **manually controlled** by a caregiver through the settings panel.

---

## System Components

### 1. Android TV App (`app/`)

The primary application, written in **Kotlin** with **Jetpack Compose for TV**.

| Layer | Key Files | Purpose |
|---|---|---|
| **Entry Point** | `MainActivity.kt`, `TalkTvApplication.kt` | TV launcher activity with child-safety back-guard |
| **Data** | `data/model/`, `data/local/`, `data/repository/` | Room database (tiles, press events, stages, settings), DAOs, auto-stage progression logic |
| **TTS & AI** | `tts/TtsManager.kt`, `tts/AiPhrasePredictor.kt` | Offline speech synthesis and AI-powered subtitle phrase expansion |
| **Player** | `player/ContentLauncher.kt`, `player/VideoPlayerActivity.kt` | TV Intent deep-links for YouTube Kids/TV apps + Media3 ExoPlayer fallback |
| **UI** | `ui/theme/`, `ui/components/`, `ui/home/`, `ui/category/`, `ui/settings/`, `ui/navigation/` | Compose-based tile grid, phrase bar, glow borders, PIN-locked caregiver settings |

**Tech stack:** Kotlin 1.9.23 · Android SDK 34 (API 26+) · Jetpack Compose TV Material · Room 2.6.1 · Media3 ExoPlayer 1.3.1 · Coil 2.6 · Java 17

### 2. Web Companion Dashboard (`dashboard/`)

A real-time caregiver and SLP hub served via Node.js.

- **Live Monitor & Aided Modeling** — Real-time mirroring of tile selections on TV
- **Clinical SLP Analytics** — Intentional vs. random press tracking, extinction curves, vocabulary growth metrics
- **Stage Curriculum Roadmap** — Visual 4-stage progression with automated gates
- **Tile Studio & Curator** — Custom video clip and tile builder with YouTube Kids URL support
- **Alberta IPP & Behaviour Support Plan** — Full student profile (EPSB Lymburn School 2025–2026), interoception de-escalation protocols, mandatory 6-month review update log
- **IEP / SLP Clinical Summary Report** — One-click printable report formatted to Alberta Education standards

### 3. Interactive TV Simulator (`preview/`)

A standalone HTML-based simulator (`talktv_simulator.html`) that reproduces the full TV remote navigation experience in a web browser — useful for demos, SLP training, and development without a physical Android TV device.

### 4. Apple 3D Glassmorphism Edition (`TalkTV_Apple3DEdition/`)

An experimental visual variant that replaces flat ARASAAC pictograms with Apple-style 3D rendered emojis and frosted glassmorphism tile cards, while preserving identical motor-plan tile positions and clinical vocabulary structure.

### 5. USB Sideload Installer (`USB-INSTALL/`)

A pre-built APK (`TalkTV-USB-Install.apk`) for installing TalkTV directly onto an Android TV device via USB and ADB, without requiring Android Studio or a build toolchain.

---

## AI-Assisted Subtitle Prediction

When Noah selects AAC tiles, an on-device AI phrase predictor expands telegraphic inputs into natural, grammatically rich closed-caption subtitles displayed directly on the TV screen:

| Tile Input | AI Subtitle Output |
|---|---|
| `"I want"` + `"Pancakes"` | *"I would like to watch pancake cooking videos, please."* |
| `"Bathroom"` | *"I need to use the washroom right away."* |
| `"All done"` | *"I am all done and ready for a break."* |

---

## Alberta IPP Review Cycle

TalkTV is integrated with Noah's **Individualized Program Plan** under Alberta Education (EPSB):

- **Baseline**: October 29, 2025
- **Review 1**: October 31, 2025
- **Mid-Year Review 2**: January 30, 2026
- **Year-End Summary**: June 23, 2026

The dashboard and app both support logging objective mastery stats, review dates, and updated IEP goals on this 6-month cycle. Interoceptive de-escalation (hunger, thirst, sensory overload, washroom) is tracked alongside communication metrics.

---

## Getting Started

### 📥 Hosted Download & Installation

- **GitHub Repository**: [kerifis/Noah](https://github.com/kerifis/Noah)
- **Latest Release (v1.2)**: [TalkTV v1.2 Release Page](https://github.com/kerifis/Noah/releases/tag/v1.2)
- **Direct APK Download**: [TalkTV-v1.2.apk](https://github.com/kerifis/Noah/releases/download/v1.2/TalkTV-v1.2.apk)

```powershell
# Install downloaded APK directly to Android TV via ADB:
adb install -r TalkTV-v1.2.apk
```

### Build & Sideload Alternatives

```powershell
# Sideload pre-built local APK from USB-INSTALL:
adb install USB-INSTALL/TalkTV-USB-Install.apk

# Or build and install from source (requires Android Studio / SDK 34 / Java 17):
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Run the Web Dashboard

```powershell
node dashboard/server.js
# Local:   http://localhost:3000
# Network: http://<your-lan-ip>:3000
```

### Open the TV Simulator

Open `preview/talktv_simulator.html` in any modern browser, or visit `http://localhost:3000/preview/talktv_simulator.html` when the dashboard server is running.

---

## Project Structure Overview

```
NOAH/
├── app/                          # Android TV AAC application (Kotlin + Compose)
│   ├── src/main/java/com/talktv/aac/
│   │   ├── MainActivity.kt      # TV launcher entry point
│   │   ├── data/                 # Room DB models, DAOs, repository
│   │   ├── tts/                  # Speech synthesis + AI phrase predictor
│   │   ├── player/               # Content launcher + ExoPlayer
│   │   └── ui/                   # Compose screens, components, theme, navigation
│   └── build.gradle.kts
├── dashboard/                    # Web companion (Node.js server + HTML/CSS/JS)
│   ├── server.js
│   ├── index.html
│   ├── app.js
│   ├── data.js
│   └── style.css
├── preview/                      # Interactive browser-based TV simulator
│   └── talktv_simulator.html
├── TalkTV_Apple3DEdition/        # Experimental 3D glassmorphism visual variant
├── USB-INSTALL/                  # Pre-built sideload APK
├── keystore/                     # Debug signing keystore
├── build.gradle.kts              # Root Gradle build
├── settings.gradle.kts           # Project settings (rootProject.name = "TalkTV")
└── README.md                     # Full technical documentation
```

---

## License & Attribution

- **ARASAAC Pictograms**: Aragonese Centre of Augmentative and Alternative Communication — [arasaac.org](https://arasaac.org)
- **Fitzgerald Key**: Color-coded vocabulary organization system for AAC
- **Alberta Education IPP Framework**: Individualized Program Plan standards for Alberta, Canada

---

*Built with love for Noah.* 💙
