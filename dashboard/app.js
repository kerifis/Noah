// TalkTV Web Companion Dashboard Application Logic

let complexityChartInstance = null;
let frequencyChartInstance = null;
let selectedTileCategory = "CARTOON";
let selectedTileIcon = "🐶";

const AVAILABLE_ICONS = [
  "🐶", "🍉", "🍳", "🎶", "🦁", "🚂", 
  "👤", "🤲", "➕", "🛑", "🔄", "❤️", 
  "🚫", "🛟", "😊", "🏠", "🌳", "🎨", 
  "👨‍👩‍👧", "🚗", "🍕", "🥛", "🍪", "🧸"
];

// Initialize on page load
document.addEventListener("DOMContentLoaded", () => {
  renderLivePhrase();
  renderEventLogTable();
  initAnalyticsCharts();
  renderCurriculumRoadmap();
  renderIconSelector();
  renderTileLibrary();
  renderIppReviewTimeline();
  startLiveIdleTimer();
  updateReportDate();
});

// Tab Navigation
function switchTab(tabId) {
  document.querySelectorAll(".nav-item").forEach(item => {
    item.classList.toggle("active", item.dataset.tab === tabId);
  });
  document.querySelectorAll(".tab-pane").forEach(pane => {
    pane.classList.toggle("active", pane.id === tabId);
  });

  // Re-render charts when switching to Analytics to ensure canvas resizing
  if (tabId === "tab-analytics") {
    setTimeout(() => {
      if (complexityChartInstance) complexityChartInstance.resize();
      if (frequencyChartInstance) frequencyChartInstance.resize();
    }, 150);
  }
}

// ----------------------------------------------------
// MODULE 1: LIVE MONITOR & CAREGIVER MODELING
// ----------------------------------------------------

function renderLivePhrase() {
  const container = document.getElementById("live-phrase-chips");
  if (!container) return;
  container.innerHTML = "";

  const phrase = TalkTvDataStore.liveState.phrase || [];

  if (phrase.length === 0) {
    container.innerHTML = `<span style="color: var(--text-muted); font-size: 16px;">TV remote phrase bar is currently idle. Ready for input...</span>`;
    return;
  }

  phrase.forEach(word => {
    const chip = document.createElement("div");
    const lower = word.toLowerCase();
    let typeClass = "show";
    if (lower === "i") typeClass = "pronoun";
    else if (lower === "want" || lower === "more" || lower === "again" || lower === "stop") typeClass = "verb";

    chip.className = `chip-word ${typeClass}`;
    chip.innerText = word;
    container.appendChild(chip);
  });
}



function modelWordOnTv(word, type) {
  speakText(word);

  // Append to phrase
  TalkTvDataStore.liveState.phrase.push(word);
  renderLivePhrase();

  // Send cross-tab message to TV simulator
  syncChannel.postMessage({
    type: "MODEL_WORD",
    word: word,
    timestamp: Date.now()
  });

  showToast(`Modeled "${word}" to Living Room TV screen`, "📢");
}

function modelCustomWord() {
  const input = document.getElementById("custom-model-input");
  const val = input.value.trim();
  if (!val) return;

  modelWordOnTv(val.toUpperCase(), "verb");
  input.value = "";
}

function startLiveIdleTimer() {
  setInterval(() => {
    const elapsedSec = Math.floor((Date.now() - TalkTvDataStore.liveState.lastEventTime) / 1000);
    const remaining = Math.max(0, TalkTvDataStore.settings.idleTimeoutSeconds - (elapsedSec % TalkTvDataStore.settings.idleTimeoutSeconds));
    const label = document.getElementById("idle-timer-status");
    if (label) {
      label.innerText = `${remaining}s / ${TalkTvDataStore.settings.idleTimeoutSeconds}s`;
    }
  }, 1000);
}

// ----------------------------------------------------
// MODULE 2: CLINICAL SLP & BEHAVIORAL ANALYTICS
// ----------------------------------------------------

function initComplexityChart() {
  const ctx = document.getElementById("complexityChart");
  if (!ctx) return;

  const labels = ["Day 1", "Day 5", "Day 10", "Day 15", "Day 20", "Day 25", "Day 30"];

  complexityChartInstance = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [
        {
          label: '2-Word Phrases ("I want")',
          data: [1, 3, 6, 11, 14, 17, 21],
          borderColor: '#00E676',
          backgroundColor: 'rgba(0, 230, 118, 0.12)',
          fill: true,
          tension: 0.35,
          borderWidth: 3,
          pointBackgroundColor: '#00E676',
          pointRadius: 5
        },
        {
          label: 'Single-Word Direct Requests',
          data: [15, 14, 12, 10, 8, 7, 5],
          borderColor: '#FFD600',
          backgroundColor: 'rgba(255, 214, 0, 0.05)',
          fill: true,
          tension: 0.35,
          borderWidth: 2,
          pointBackgroundColor: '#FFD600',
          pointRadius: 4
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          labels: { color: '#8B9CB5', font: { family: 'Inter', size: 12, weight: '600' } }
        },
        tooltip: {
          backgroundColor: '#0F1829',
          titleColor: '#00E5FF',
          bodyColor: '#FFF',
          borderColor: '#2B3E60',
          borderWidth: 1,
          padding: 12
        }
      },
      scales: {
        x: {
          grid: { color: 'rgba(44, 62, 95, 0.25)' },
          ticks: { color: '#596B82', font: { family: 'Inter', size: 11 } }
        },
        y: {
          grid: { color: 'rgba(44, 62, 95, 0.25)' },
          ticks: { color: '#596B82', font: { family: 'Inter', size: 11 } }
        }
      }
    }
  });
}

function initFrequencyChart() {
  const ctx = document.getElementById("frequencyChart");
  if (!ctx) return;

  frequencyChartInstance = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['"I" (Core)', '"want" (Core)', '"more" (Core)', '"Cartoons"', '"Cooking"', '"stop" (Core)'],
      datasets: [{
        label: 'Selections Count',
        data: [210, 185, 94, 68, 52, 38],
        backgroundColor: [
          '#FFCA28', // Yellow (Pronoun)
          '#4CAF50', // Green (Verb)
          '#00E676', // Bright Green (Verb)
          '#FFA726', // Orange (Category)
          '#FF9800', // Orange (Show)
          '#EF5350'  // Red (Stop)
        ],
        borderRadius: 8,
        borderWidth: 1,
        borderColor: 'rgba(255, 255, 255, 0.15)'
      }]
    },
    options: {
      indexAxis: 'y',
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          backgroundColor: '#0F1829',
          titleColor: '#FFD600',
          bodyColor: '#FFF',
          borderColor: '#2B3E60',
          borderWidth: 1
        }
      },
      scales: {
        x: {
          grid: { color: 'rgba(44, 62, 95, 0.25)' },
          ticks: { color: '#596B82', font: { family: 'Inter', size: 11 } }
        },
        y: {
          grid: { display: false },
          ticks: { color: '#FFF', font: { family: 'Inter', size: 12, weight: '700' } }
        }
      }
    }
  });
}

function renderEventLogTable() {
  const tbody = document.getElementById("event-log-tbody");
  if (!tbody) return;
  tbody.innerHTML = "";

  TalkTvDataStore.events.slice(0, 15).forEach(evt => {
    const tr = document.createElement("tr");
    const d = new Date(evt.timestamp);
    const timeStr = d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

    tr.innerHTML = `
      <td style="color: var(--text-muted); font-size: 13px;">${timeStr}</td>
      <td style="font-weight: 700; color: #FFF;">${evt.word.toUpperCase()}</td>
      <td style="color: var(--accent-cyan); font-weight: 600;">"${evt.phrase.join(" ")}"</td>
      <td>
        <span class="log-badge ${evt.wasReward ? 'reward' : 'speech'}">
          ${evt.wasReward ? '★ Reward: ' + (evt.show || 'Show Launched') : 'Speech Output'}
        </span>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

function exportLogCsv() {
  let csv = "Timestamp,TileWord,FullPhrase,WasRewardTriggered,ContentShow\n";
  TalkTvDataStore.events.forEach(e => {
    csv += `"${new Date(e.timestamp).toISOString()}","${e.word}","${e.phrase.join(" ")}",${e.wasReward},"${e.show || ''}"\n`;
  });

  const blob = new Blob([csv], { type: "text/csv" });
  const url = URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = `talktv_aac_clinical_events_${Date.now()}.csv`;
  a.click();
  showToast("CSV Log Downloaded", "📊");
}

// ----------------------------------------------------
// MODULE 3: STAGE CURRICULUM & ROADMAP
// ----------------------------------------------------

function renderStageCards() {
  const container = document.getElementById("stages-cards-container");
  if (!container) return;
  container.innerHTML = "";

  TalkTvDataStore.stages.forEach(stg => {
    const card = document.createElement("div");
    const isActive = stg.stageNumber === TalkTvDataStore.settings.currentStage;
    card.className = `stage-card ${isActive ? 'active' : ''} ${stg.status}`;

    const progressPct = Math.min(100, Math.round((stg.completedPresses / stg.requiredPresses) * 100));

    card.innerHTML = `
      <div class="stage-status-badge ${stg.status}">
        ${stg.status === 'mastered' ? '✓ Mastered (15/15)' : stg.status === 'active' ? '● Stage Active on TV' : '🔒 ' + stg.status}
      </div>

      <div class="stage-num-title">Stage ${stg.stageNumber}: ${stg.title}</div>
      <div class="stage-desc">${stg.description}</div>

      <div style="font-size: 12px; font-weight: 700; color: var(--text-secondary); display: flex; justify-content: space-between;">
        <span>Progress towards mastery:</span>
        <span style="color: var(--accent-cyan);">${stg.completedPresses} / ${stg.requiredPresses} (${progressPct}%)</span>
      </div>
      <div class="stage-progress-bar">
        <div class="stage-progress-fill" style="width: ${progressPct}%;"></div>
      </div>

      <div style="font-size: 11px; font-weight: 800; color: var(--accent-yellow); margin-top: 10px;">UNLOCKED TILES:</div>
      <div class="stage-pills-wrap">
        ${stg.tiles.map(t => `<span class="stage-mini-pill">${t}</span>`).join("")}
      </div>

      <button class="btn-stage-action ${isActive ? 'primary' : 'secondary'}" onclick="activateStage(${stg.stageNumber})">
        ${isActive ? 'Active Curriculum' : 'Switch to Stage ' + stg.stageNumber}
      </button>
    `;

    container.appendChild(card);
  });
}

function activateStage(stageNum) {
  TalkTvDataStore.settings.currentStage = stageNum;
  TalkTvDataStore.stages.forEach(s => {
    if (s.stageNumber < stageNum) s.status = "mastered";
    else if (s.stageNumber === stageNum) s.status = "active";
    else s.status = "upcoming";
  });
  TalkTvDataStore.save();
  renderStageCards();

  syncChannel.postMessage({
    type: "STAGE_CHANGE",
    stage: stageNum
  });

  showToast(`Switched TV curriculum to Stage ${stageNum}`, "🗺️");
}

function toggleAutoUnlock(checked) {
  TalkTvDataStore.settings.autoUnlockEnabled = checked;
  TalkTvDataStore.save();
  showToast(`Automatic stage progression ${checked ? 'Enabled' : 'Disabled'}`, "⚙️");
}

// ----------------------------------------------------
// MODULE 4: TILE STUDIO & CUSTOM SHOW CURATOR
// ----------------------------------------------------

function renderIconSelector() {
  const grid = document.getElementById("icon-selector-grid");
  if (!grid) return;
  grid.innerHTML = "";

  AVAILABLE_ICONS.forEach(icon => {
    const btn = document.createElement("button");
    btn.type = "button";
    btn.className = `icon-pick-btn ${icon === selectedTileIcon ? 'selected' : ''}`;
    btn.innerText = icon;
    btn.onclick = () => {
      document.querySelectorAll(".icon-pick-btn").forEach(b => b.classList.remove("selected"));
      btn.classList.add("selected");
      selectedTileIcon = icon;
    };
    grid.appendChild(btn);
  });
}

function selectTileCategory(cat, el) {
  document.querySelectorAll(".cat-pill").forEach(p => p.classList.remove("active"));
  el.classList.add("active");
  selectedTileCategory = cat;
}

function renderTileLibrary() {
  const container = document.getElementById("tile-library-container");
  const countLabel = document.getElementById("tile-library-count");
  if (!container) return;
  container.innerHTML = "";

  countLabel.innerText = `${TalkTvDataStore.tiles.length} Active Tiles`;

  TalkTvDataStore.tiles.forEach(tile => {
    const card = document.createElement("div");
    card.className = "library-card";
    card.innerHTML = `
      <div style="width: 100%; display: flex; justify-content: space-between; align-items: center;">
        <span style="font-size: 10px; font-weight: 800; color: ${tile.color};">Stage ${tile.stage}</span>
        <button onclick="deleteTile('${tile.id}')" style="background: none; border: none; color: var(--accent-red); cursor: pointer; font-size: 14px;" title="Delete Tile">✕</button>
      </div>
      <div class="library-icon">${tile.icon}</div>
      <div class="library-word">${tile.word}</div>
      <div class="library-cat">${tile.category} • ${tile.action === 'SPEAK_AND_LAUNCH' ? 'Show Launch' : 'Speech'}</div>
    `;
    container.appendChild(card);
  });
}

function saveNewTile() {
  const wordInput = document.getElementById("new-tile-word");
  const uriInput = document.getElementById("new-tile-uri");
  const word = wordInput.value.trim();

  if (!word) {
    alert("Please enter a word or show label");
    return;
  }

  const newTile = {
    id: `custom_${Date.now()}`,
    word: word,
    icon: selectedTileIcon,
    category: selectedTileCategory,
    color: selectedTileCategory === "CARTOON" ? "#FFA726" : selectedTileCategory === "FOOD" ? "#FF9800" : "#AB47BC",
    stage: TalkTvDataStore.settings.currentStage,
    isCore: false,
    action: uriInput.value.trim() ? "SPEAK_AND_LAUNCH" : "SPEAK_ONLY",
    contentUri: uriInput.value.trim() || null
  };

  TalkTvDataStore.tiles.push(newTile);
  TalkTvDataStore.save();
  renderTileLibrary();

  // Reset form
  wordInput.value = "";
  uriInput.value = "";

  showToast(`Added "${word}" to TV vocabulary library!`, "✓");
}

function deleteTile(tileId) {
  TalkTvDataStore.tiles = TalkTvDataStore.tiles.filter(t => t.id !== tileId);
  TalkTvDataStore.save();
  renderTileLibrary();
  showToast("Tile removed from library", "🗑️");
}

function testSpeechVoice() {
  const wordInput = document.getElementById("new-tile-word");
  const word = wordInput.value.trim() || "Bluey Cooking Special";
  speakText(word);
}

function updateVideoPreview(url) {
  // Can be extended to display iframe or thumbnail
}

// ----------------------------------------------------
// UTILITIES & REALTIME HELPERS
// ----------------------------------------------------

function speakText(text) {
  if ('speechSynthesis' in window) {
    window.speechSynthesis.cancel();
    const utterance = new SpeechSynthesisUtterance(text);
    utterance.rate = TalkTvDataStore.settings.ttsSpeechRate;
    utterance.pitch = TalkTvDataStore.settings.ttsPitch;
    window.speechSynthesis.speak(utterance);
  }
}

function showToast(message, icon = "✓") {
  const toast = document.getElementById("toast-notification");
  const iconSpan = document.getElementById("toast-icon");
  const textSpan = document.getElementById("toast-text");

  if (!toast) return;
  iconSpan.innerText = icon;
  textSpan.innerText = message;
  toast.style.display = "flex";

  setTimeout(() => {
    toast.style.display = "none";
  }, 3200);
}

function updateReportDate() {
  const el = document.getElementById("report-date");
  if (el) {
    el.innerText = new Date().toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' });
  }
}

// ----------------------------------------------------
// MODULE 5: ALBERTA IPP & 6-MONTH REVIEW TRACKER
// ----------------------------------------------------

function renderIppReviewTimeline() {
  const container = document.getElementById("ipp-review-timeline-container");
  if (!container) return;
  container.innerHTML = "";

  const entries = (TalkTvDataStore.ippProfile && TalkTvDataStore.ippProfile.reviewEntries) || [];

  entries.forEach((entry) => {
    const isUpcoming = entry.status && entry.status.toLowerCase().includes("upcoming");
    const card = document.createElement("div");
    card.className = `review-entry-card ${isUpcoming ? "upcoming" : ""}`;

    card.innerHTML = `
      <div class="review-entry-header">
        <div>
          <strong style="font-size: 16px; color: #FFF;">${entry.title || entry.type}</strong>
          <span style="font-size: 13px; color: var(--accent-cyan); margin-left: 8px;">• ${entry.date}</span>
        </div>
        <span class="review-tag ${isUpcoming ? "upcoming" : ""}">${entry.status || "Recorded"}</span>
      </div>
      <div style="font-size: 12px; color: var(--text-muted); margin-bottom: 8px;">
        Evaluator / Reviewer: <strong style="color: #FFF;">${entry.reviewer || "Learning Team"}</strong>
      </div>
      <p style="font-size: 13px; color: var(--text-secondary); line-height: 1.6; margin: 0;">
        ${entry.summary || entry.content || entry.notes || ""}
      </p>
    `;
    container.appendChild(card);
  });
}

function openIppReviewModal() {
  const modal = document.getElementById("modal-ipp-review");
  if (!modal) return;
  const dateInput = document.getElementById("review-modal-date");
  if (dateInput) {
    dateInput.value = new Date().toISOString().split('T')[0];
  }
  const authorInput = document.getElementById("review-modal-author");
  if (authorInput) {
    authorInput.value = "Zayd Rutkowski (Homeroom Teacher)";
  }
  modal.style.display = "flex";
}

function closeIppReviewModal() {
  const modal = document.getElementById("modal-ipp-review");
  if (modal) modal.style.display = "none";
}

function submitIppReview() {
  const typeSelect = document.getElementById("review-modal-type");
  const dateInput = document.getElementById("review-modal-date");
  const authorInput = document.getElementById("review-modal-author");
  const notesInput = document.getElementById("review-modal-notes");

  const notes = notesInput ? notesInput.value.trim() : "";
  if (!notes) {
    alert("Please enter clinical review observations or notes.");
    return;
  }

  const rawDate = dateInput ? dateInput.value : "";
  let formattedDate = rawDate;
  if (rawDate) {
    const parts = rawDate.split("-");
    const d = new Date(parts[0], parts[1] - 1, parts[2]);
    formattedDate = d.toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' });
  }

  const newEntry = {
    id: `rev_${Date.now()}`,
    title: typeSelect ? typeSelect.value : "6-Month Progress Review",
    date: formattedDate || "Recent Review",
    status: "Official Record",
    reviewer: authorInput ? authorInput.value.trim() : "Learning Team",
    summary: notes
  };

  TalkTvDataStore.addIppReview(newEntry);
  renderIppReviewTimeline();
  closeIppReviewModal();
  if (notesInput) notesInput.value = "";

  showToast("6-Month Review logged to Alberta IPP Record!", "✓");
}


