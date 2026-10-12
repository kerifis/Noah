# TalkTV — Apple 3D Emoji & VisionOS Glassmorphism Edition 🍎✨

> **A 10-foot Augmentative & Alternative Communication (AAC) Interface for Android TV, combining Apple design language with clinical motor planning consistency and Alberta IPP interoception support.**

---

## 🎨 Visual Design Philosophy

TalkTV Apple 3D Edition elevates AAC on television from clinical utility to a breathtaking, tactile consumer electronics experience. It replaces compulsive remote channel surfing by turning intentional D-pad navigation into an irresistible, rewarding sensory journey.

### 1. High-Resolution 3D Emoji Assets
- Replaces flat, clinical 2D pictograms with **Apple-style 3D rendered emojis** featuring realistic physical lighting, soft specular highlights, and subsurface scattering.
- Transparent WebP/PNG assets with depth cues make vocabulary items immediately recognizable from a 10-foot viewing distance across the living room.

### 2. Frosted Glassmorphism with Modified Fitzgerald Key
Preserves linguistic scaffolding while delivering visionOS / tvOS aesthetics:
- **Pronouns (Yellow)**: Soft frosted gold glass (`#FFF9C4` ➔ `#FBC02D` gradient, 85% opacity, `backdrop-filter: blur(20px)`).
- **Verbs (Green)**: Frosted emerald mint (`#C8E6C9` ➔ `#388E3C` gradient).
- **Nouns / Content Reinforcers (Orange/Peach)**: Frosted apricot nectar (`#FFE0B2` ➔ `#F57C00` gradient).
- **Feelings / Interoception (Cyan / Rose)**: Frosted periwinkle and gentle coral (`#E1F5FE` and `#FFCDD2`).

### 3. Apple Squircle & Locked Spatial Motor Consistency
- Built with Jetpack Compose `RoundedCornerShape(28.dp)` to achieve the continuous-curvature Apple "squircle".
- **Strict Motor-Plan Locking**: Coordinates for core words never change between screens. The child develops automatic muscle memory on the TV remote D-pad.

### 4. Tactile 3D D-Pad Focus & Elevation
- **Scale Pop**: Highlighting a tile scales it outward by `1.12x` (`animateFloatAsState`) with spring physics.
- **Luminous Rim Glow**: Radial gradient glowing halo matching the category color illuminates the surrounding dark glass.
- **Crisp High-Contrast Border**: 3dp solid neon outline ensures effortless visual tracking from across the room.
- **AI Subtitle Bar**: Displays expanded natural language predictions in closed-caption VisionOS style before speech synthesis and video playback.
