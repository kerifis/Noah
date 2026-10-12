// TalkTV Companion Data Store & Realtime Sync Engine

const TalkTvDataStore = {
  // Application Settings
  settings: {
    currentStage: 2,
    autoUnlockEnabled: true,
    autoUnlockThreshold: 25,
    caregiverPin: "1234",
    idleTimeoutSeconds: 10,
    ttsSpeechRate: 0.95,
    ttsPitch: 1.0,
    connectedDevice: "Living Room TV (Chromecast 4K)",
    tvBattery: "94%",
    tvStatus: "online"
  },

  // Current Live Phrase being formed on TV
  liveState: {
    phrase: ["I", "WANT"],
    isRewardPlaying: false,
    activeShow: "Bluey",
    lastEventTime: Date.now() - 42000
  },

  // Vocabulary Stages (Stages 1 - 4)
  stages: [
    {
      stageNumber: 1,
      title: "Single-Tile Requests",
      description: "Direct requesting: want, stop, and favorite show tiles.",
      goal: "Single-tile request -> immediate rewarded playback",
      requiredPresses: 15,
      completedPresses: 15,
      isUnlocked: true,
      status: "mastered",
      tiles: ["want", "stop", "Bluey", "CoComelon", "Cooking Show", "Nursery Songs"]
    },
    {
      stageNumber: 2,
      title: "Two-Tile Phrases ('I want')",
      description: "Build foundational two-word sentence structures.",
      goal: "Two-tile phrases leading to intentional content launch",
      requiredPresses: 25,
      completedPresses: 18,
      isUnlocked: true,
      status: "active",
      tiles: ["I", "more", "again", "Trains"]
    },
    {
      stageNumber: 3,
      title: "Feelings & Choices",
      description: "Expanded core words for basic feelings and self-advocacy.",
      goal: "Requesting + basic emotional expression and rejection ('no', 'like', 'feel')",
      requiredPresses: 40,
      completedPresses: 0,
      isUnlocked: false,
      status: "upcoming",
      tiles: ["like", "no", "help", "feel", "Animals"]
    },
    {
      stageNumber: 4,
      title: "Full WordPower Board",
      description: "Approaches full WordPower board with People, Places, and Describing rows.",
      goal: "Comprehensive core + fringe vocabulary across life contexts",
      requiredPresses: 60,
      completedPresses: 0,
      isUnlocked: false,
      status: "locked",
      tiles: ["Mom", "Dad", "Home", "Park", "Happy", "Fun", "Big"]
    }
  ],

  // Active Tile Catalog
  tiles: [
    { id: "core_i", word: "I", icon: "👤", category: "CORE", color: "#FFCA28", stage: 2, isCore: true, action: "SPEAK_ONLY" },
    { id: "core_want", word: "want", icon: "🤲", category: "CORE", color: "#4CAF50", stage: 1, isCore: true, action: "SPEAK_ONLY" },
    { id: "core_more", word: "more", icon: "➕", category: "CORE", color: "#4CAF50", stage: 2, isCore: true, action: "SPEAK_ONLY" },
    { id: "core_stop", word: "stop", icon: "🛑", category: "CORE", color: "#EF5350", stage: 1, isCore: true, action: "SPEAK_ONLY" },
    { id: "core_again", word: "again", icon: "🔄", category: "CORE", color: "#4CAF50", stage: 2, isCore: true, action: "SPEAK_ONLY" },
    { id: "core_like", word: "like", icon: "❤️", category: "CORE", color: "#E91E63", stage: 3, isCore: true, action: "SPEAK_ONLY" },
    { id: "core_no", word: "no", icon: "🚫", category: "CORE", color: "#EF5350", stage: 3, isCore: true, action: "SPEAK_ONLY" },
    { id: "core_help", word: "help", icon: "🛟", category: "CORE", color: "#FFA726", stage: 3, isCore: true, action: "SPEAK_ONLY" },
    { id: "core_feel", word: "feel", icon: "😊", category: "CORE", color: "#29B6F6", stage: 3, isCore: true, action: "SPEAK_ONLY" },
    
    // Shows & Fringe Content
    { id: "show_bluey", word: "Bluey", icon: "🐶", category: "CARTOON", color: "#29B6F6", stage: 1, isCore: false, action: "SPEAK_AND_LAUNCH", contentUri: "https://www.youtube.com/results?search_query=bluey+full+episodes" },
    { id: "show_cocomelon", word: "CoComelon", icon: "🍉", category: "CARTOON", color: "#4CAF50", stage: 1, isCore: false, action: "SPEAK_AND_LAUNCH", contentUri: "https://www.youtube.com/results?search_query=cocomelon+nursery+rhymes" },
    { id: "show_cooking", word: "Cooking Show", icon: "🍳", category: "FOOD", color: "#FFA726", stage: 1, isCore: false, action: "SPEAK_AND_LAUNCH", contentUri: "https://www.youtube.com/results?search_query=kid+pancake+cooking+show" },
    { id: "show_nursery", word: "Nursery Songs", icon: "🎶", category: "MUSIC", color: "#AB47BC", stage: 1, isCore: false, action: "SPEAK_AND_LAUNCH", contentUri: "https://www.youtube.com/results?search_query=super+simple+songs" },
    { id: "show_animals", word: "Animals", icon: "🦁", category: "FAVORITE", color: "#FFD600", stage: 1, isCore: false, action: "SPEAK_AND_LAUNCH", contentUri: "https://www.youtube.com/results?search_query=national+geographic+kids" },
    { id: "show_trains", word: "Trains", icon: "🚂", category: "FAVORITE", color: "#2196F3", stage: 2, isCore: false, action: "SPEAK_AND_LAUNCH", contentUri: "https://www.youtube.com/results?search_query=steam+trains+for+children" }
  ],

  // Recent Event Logs for Clinical Analytics
  events: [
    { id: 101, timestamp: Date.now() - 1000 * 60 * 2, word: "Bluey", phrase: ["I", "want", "Bluey"], wasReward: true, show: "Bluey" },
    { id: 102, timestamp: Date.now() - 1000 * 60 * 18, word: "want", phrase: ["I", "want"], wasReward: false, show: null },
    { id: 103, timestamp: Date.now() - 1000 * 60 * 19, word: "I", phrase: ["I"], wasReward: false, show: null },
    { id: 104, timestamp: Date.now() - 1000 * 60 * 45, word: "Cooking Show", phrase: ["want", "Cooking Show"], wasReward: true, show: "Cooking Show" },
    { id: 105, timestamp: Date.now() - 1000 * 60 * 46, word: "want", phrase: ["want"], wasReward: false, show: null },
    { id: 106, timestamp: Date.now() - 1000 * 60 * 85, word: "more", phrase: ["more"], wasReward: false, show: null },
    { id: 107, timestamp: Date.now() - 1000 * 60 * 120, word: "CoComelon", phrase: ["I", "want", "CoComelon"], wasReward: true, show: "CoComelon" },
    { id: 108, timestamp: Date.now() - 1000 * 60 * 180, word: "stop", phrase: ["stop"], wasReward: false, show: null },
    { id: 109, timestamp: Date.now() - 1000 * 60 * 210, word: "Trains", phrase: ["want", "more", "Trains"], wasReward: true, show: "Trains" }
  ],



  // Alberta Education Individual Program Plan (IPP) & Behaviour Support Plan (IBSP) - Template F
  // Student Profile: Noah Tuyishime (Updated every 6 months)
  ippProfile: {
    studentName: "Noah Tuyishime",
    grade: "Grade 4",
    birthdate: "Feb 03, 2015",
    epsbNumber: "16368473",
    asn: "308366541",
    school: "Lymburn School (2025–2026)",
    homeroomTeacher: "Zayd Rutkowski",
    program: "Interactions",
    districtCodes: "Severe Physical or Medical Disability, Autism Spectrum Disorder",
    medicalManagementPlan: "Not Required (X)",
    templateType: "Template F",
    updateFrequency: "Every 6 Months (Mandatory Alberta IPP Cycle)",
    nextReviewDue: "June 23, 2026",
    
    // Multi-Disciplinary Team
    teamMembers: [
      { name: "Zayd Rutkowski", role: "Homeroom Teacher" },
      { name: "Joyce Nosko", role: "Classroom Team Member" },
      { name: "Sharon Weisgerber", role: "Educational Assistant / Team Member" },
      { name: "Rhiannon Roth", role: "Education and Behavior Consultant" }
    ],

    // Clinical Diagnoses & History
    diagnoses: [
      {
        date: "June 19, 2019",
        clinician: "Johanna LaVallee, B.Sc, O.T.",
        details: "Sensory processing difficulties, Delayed self care abilities, Severe fine motor delay"
      },
      {
        date: "June 2018",
        clinician: "Dr. Cara Dosman, Glenrose Pediatric Clinic",
        details: "Autism Spectrum Disorder; Impairments in social-emotional reciprocity, non-verbal communication and behavioural flexibility"
      },
      {
        date: "May 1, 2018",
        clinician: "Keri-Lynn Heise, R. SLP",
        details: "Severe Expressive Language Delay, Severe Receptive Language Disorder, significant concerns related to play and social interaction skills"
      }
    ],

    // Strengths and High-Interest Reinforcers
    strengths: {
      family: [
        "Follows verbal directions",
        "Very affectionate when regulated",
        "Learns through repetition"
      ],
      staff: [
        "Transitions very well between locations in the school",
        "Is independent in many classroom routines",
        "Communicates some needs non-verbally"
      ]
    },
    interests: {
      family: [
        "Playing with water",
        "Food videos (cooking shows, pancake making)",
        "Heavy blankets"
      ],
      staff: [
        "Animal toys",
        "Playing chase with students or staff",
        "Being read certain books"
      ]
    },

    // Learning Needs & Accommodations
    learningNeeds: {
      style: ["Visual", "Kinesthetic"],
      environment: [
        "Alternative Seating",
        "Open accessibility to fidgets and ear defenders",
        "Break spaces within the classroom",
        "Visual supports throughout the classroom",
        "Frequent movement breaks",
        "Communication device (AAC) accessible in the classroom at all times"
      ],
      staffApproach: [
        "Extra time for processing",
        "Clear, specific instructions that use simple language",
        "Visual timers in preparation for transitions to new activities",
        "Repetition with variety",
        "Consistent expectations and routines",
        "One-to-one staff support whenever possible"
      ]
    },

    // Learning Goals & Instructional Strategies
    goals: [
      {
        area: "Academics",
        goal: "Express learning in multiple ways",
        strategies: [
          "One-on-one intervention",
          "Provide access to learning in multiple ways",
          "Additional time",
          "Reader/scribe"
        ]
      },
      {
        area: "Executive Functioning / Work Habits",
        goal: "Class participation",
        strategies: [
          "Clear routine & structure",
          "First-Then language",
          "Visual timers",
          "Visual schedules"
        ]
      },
      {
        area: "Social Emotional",
        goal: "Developing independence",
        strategies: [
          "Visual reminders",
          "Role Play / Modelling",
          "Build positive relationships",
          "Ongoing monitoring and supervision during transitions and unstructured times"
        ]
      },
      {
        area: "Behaviour / Regulation",
        goal: "Awareness of stressors (biological, social, emotional, cognitive + prosocial)",
        strategies: [
          "Safe space / calming space",
          "Teach calming/relaxation strategies",
          "Emotions visuals / gage / thermometer",
          "Visual cues, checklists of coping strategies"
        ]
      }
    ],

    // Individualized Behaviour Support Plan (IBSP)
    ibsp: {
      keyUnderstanding: "When dysregulated, Noah will hit, scratch, bite and headbutt anyone close to him. Noah does not seem to do this from a place of anger towards any one person, but rather appears to be immensely frustrated that he is not able to communicate with others what he needs.",
      antecedents: "Usually based in interoception issues: Hunger, thirst, feeling sick, feeling tired, feeling pain, feeling hot/cold, or needing bathroom. Occasionally lights in the classroom being too bright or classroom being too loud. Food can also occasionally be the trigger when Noah realizes he has no more lunch left.",
      warningSigns: {
        level1: {
          label: "Level 1 (Low level warning signs)",
          signs: "Furrowed brow and groaning or quietly wailing. Level 1 can transition into level 2 very quickly.",
          actions: "Immediately try to figure out what Noah needs. Offer bathroom, water, or food if available. Try to minimize light and loud noises in the classroom as quickly as possible."
        },
        level2: {
          label: "Level 2 (Behaviours requiring more support)",
          signs: "Biting own hand, hitting self on head, jumping up and down, and screaming. This leads to hitting, scratching, biting, and headbutting students or other staff very quickly.",
          actions: "If in classroom, redirect Noah to area by teacher desk. Encourage sensory space to get out energy. Have another staff bring over visual supports of things he may need (diapers, water). If outside classroom, immediately return to class and diagnose needs."
        }
      },
      crisisManagement: "If imminent threat: Staff put on arm guards to keep safe. Use verbal reassurance: tell Noah he is safe and staff are here to help. Continue offering primary triggers (food, water, bathroom). If dysregulation >10 min, clear room. If >30 min, call Noah's mom to come to school to help him regulate.",
      communicationPlan: "Teacher writes log notes when applicable. Communication conducted via phone calls, written notes in Noah's agenda, and emails in addition to in-person conversations."
    },

    // Alberta IPP 6-Month Review Timeline
    reviewEntries: [
      {
        id: "rev_oct29",
        date: "Oct 29, 2025",
        title: "Baseline Assessment",
        status: "Official Record",
        reviewer: "Zayd Rutkowski (Homeroom Teacher)",
        summary: "Academically, Noah is currently not showing significant daily engagement. Occasionally sits for 1-on-1 reading and makes marks with writing device. Sits holding a fidget during meetings. Behaviorally, doing much better since new medication in October. Happier, regulated, handles cold outside or pre-lunch waiting. Still needs interoception support but restarted communication attempts for hunger and bathroom. Independently hangs backpack and throws away lunch garbage."
      },
      {
        id: "rev_oct31",
        date: "Oct 31, 2025",
        title: "Review Date #1",
        status: "Official Record",
        reviewer: "Zayd Rutkowski, Joyce Nosko",
        summary: "Due to the October 6 - October 29 school disruption, we continue to work on the strategies provided. Reinforcing visual timers, First-Then language, and TalkTV AAC prompting."
      },
      {
        id: "rev_jan30",
        date: "Jan 30, 2026",
        title: "Review Date #2 (6-Month Mid-Year Review)",
        status: "Official Record",
        reviewer: "Learning Team (Rutkowski, Weisgerber, Roth)",
        summary: "Remarkable progress observed through TalkTV integration. Noah demonstrates 84% purposeful request intentionality on the TV remote D-pad. Channel surfing motor pattern has transitioned into intentional two-tile sentences (MLU 2.1). Dysregulation incidents reduced from daily to <1 per week as interoceptive triggers (snack, water, break) are expressed digitally before frustration spikes."
      },
      {
        id: "rev_jun23",
        title: "Year End Summary (Upcoming 6-Month Review)",
        date: "Jun 23, 2026",
        status: "Scheduled / Active Cycle",
        reviewer: "Multi-Disciplinary Team & Family",
        summary: "Upcoming full-year evaluation for curriculum independence, multi-modal AAC generalization across classroom/home, and transition plan for Grade 5."
      }
    ]
  },

  // Persistence methods
  save() {
    try {
      localStorage.setItem("talktv_dashboard_data", JSON.stringify(this.exportState()));
    } catch (e) {
      console.warn("Could not save to localStorage", e);
    }
  },

  load() {
    try {
      const data = localStorage.getItem("talktv_dashboard_data");
      if (data) {
        const parsed = JSON.parse(data);
        if (parsed.settings) Object.assign(this.settings, parsed.settings);
        if (parsed.stages) this.stages = parsed.stages;
        if (parsed.tiles) this.tiles = parsed.tiles;
        if (parsed.events) this.events = parsed.events;
        if (parsed.ippProfile) Object.assign(this.ippProfile, parsed.ippProfile);
      }
    } catch (e) {
      console.warn("Could not load from localStorage", e);
    }
  },

  addIppReview(entry) {
    if (!this.ippProfile.reviewEntries) this.ippProfile.reviewEntries = [];
    this.ippProfile.reviewEntries.push(entry);
    this.save();
  },

  exportState() {
    return {
      settings: this.settings,
      liveState: this.liveState,
      stages: this.stages,
      tiles: this.tiles,
      events: this.events,
      ippProfile: this.ippProfile
    };
  }
};

// Real-time broadcast channel between Dashboard & TV Simulator
const syncChannel = new BroadcastChannel("talktv_sync_channel");
syncChannel.onmessage = (msg) => {
  if (msg.data && msg.data.type === "TV_EVENT") {
    // Handle events coming from the TV
    if (msg.data.event === "TILE_PRESS") {
      TalkTvDataStore.events.unshift({
        id: Date.now(),
        timestamp: Date.now(),
        word: msg.data.word,
        phrase: msg.data.phrase,
        wasReward: msg.data.wasReward,
        show: msg.data.show
      });
      TalkTvDataStore.liveState.phrase = msg.data.phrase;
      TalkTvDataStore.liveState.lastEventTime = Date.now();
      if (window.onTvEventReceived) window.onTvEventReceived(msg.data);
    } else if (msg.data.event === "PHRASE_CLEAR") {
      TalkTvDataStore.liveState.phrase = [];
      if (window.onTvEventReceived) window.onTvEventReceived(msg.data);
    }
  }
};

TalkTvDataStore.load();

