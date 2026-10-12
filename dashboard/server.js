// Zero-dependency local Node.js server for TalkTV Companion Dashboard
const http = require('http');
const fs = require('fs');
const path = require('path');
const os = require('os');

const PORT = process.env.PORT || 3000;
const DASHBOARD_DIR = __dirname;
const PROJECT_DIR = path.resolve(__dirname, '..');

// MIME type map
const MIME_TYPES = {
  '.html': 'text/html; charset=UTF-8',
  '.css': 'text/css; charset=UTF-8',
  '.js': 'application/javascript; charset=UTF-8',
  '.json': 'application/json; charset=UTF-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon'
};

// In-memory clinical state & active SSE client list
let activeClients = [];
let tvSharedState = {
  currentStage: 2,
  phrase: ["I", "WANT"],
  lastWord: "WANT",
  rewardTriggered: false,
  timestamp: Date.now()
};

const server = http.createServer((req, res) => {
  const parsedUrl = new URL(req.url, `http://${req.headers.host}`);
  let pathname = parsedUrl.pathname;

  // CORS headers for local LAN device access
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');

  if (req.method === 'OPTIONS') {
    res.writeHead(204);
    res.end();
    return;
  }

  // 1. API Endpoint: State Sync
  if (pathname === '/api/state') {
    if (req.method === 'GET') {
      res.writeHead(200, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify(tvSharedState));
      return;
    } else if (req.method === 'POST') {
      let body = '';
      req.on('data', chunk => body += chunk);
      req.on('end', () => {
        try {
          const data = JSON.parse(body);
          tvSharedState = Object.assign(tvSharedState, data);
          // Broadcast to connected SSE clients
          broadcastSse({ type: 'STATE_UPDATE', state: tvSharedState });
          res.writeHead(200, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({ success: true, state: tvSharedState }));
        } catch (e) {
          res.writeHead(400, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({ error: 'Invalid JSON' }));
        }
      });
      return;
    }
  }

  // 2. Real-Time Server-Sent Events (SSE) Stream
  if (pathname === '/api/stream') {
    res.writeHead(200, {
      'Content-Type': 'text/event-stream',
      'Cache-Control': 'no-cache',
      'Connection': 'keep-alive'
    });
    res.write(`data: ${JSON.stringify({ type: 'INIT', state: tvSharedState })}\n\n`);
    activeClients.push(res);

    req.on('close', () => {
      activeClients = activeClients.filter(c => c !== res);
    });
    return;
  }

  // 3. Static File Serving
  if (pathname === '/') pathname = '/index.html';

  let filePath = path.join(DASHBOARD_DIR, pathname);

  // Fallback to project root for simulator files (e.g. /preview/talktv_simulator.html)
  if (!fs.existsSync(filePath)) {
    const fallbackPath = path.join(PROJECT_DIR, pathname);
    if (fs.existsSync(fallbackPath)) {
      filePath = fallbackPath;
    }
  }

  fs.stat(filePath, (err, stats) => {
    if (err || !stats.isFile()) {
      res.writeHead(404, { 'Content-Type': 'text/plain; charset=UTF-8' });
      res.end('404 Not Found - TalkTV Companion');
      return;
    }

    const ext = path.extname(filePath).toLowerCase();
    const contentType = MIME_TYPES[ext] || 'application/octet-stream';

    res.writeHead(200, { 'Content-Type': contentType });
    fs.createReadStream(filePath).pipe(res);
  });
});

function broadcastSse(payload) {
  const msg = `data: ${JSON.stringify(payload)}\n\n`;
  activeClients.forEach(client => client.write(msg));
}

// Find local network IP address
function getLocalIp() {
  const interfaces = os.networkInterfaces();
  for (const name of Object.keys(interfaces)) {
    for (const iface of interfaces[name]) {
      if ('IPv4' === iface.family && !iface.internal) {
        return iface.address;
      }
    }
  }
  return 'localhost';
}

server.listen(PORT, () => {
  const localIp = getLocalIp();
  console.log(`\n========================================================`);
  console.log(`💬 TalkTV Web Companion Dashboard is LIVE!`);
  console.log(`👉 Local URL:     http://localhost:${PORT}`);
  console.log(`📱 Phone / LAN:   http://${localIp}:${PORT}`);
  console.log(`📺 TV Emulator:   http://localhost:${PORT}/preview/talktv_simulator.html`);
  console.log(`========================================================\n`);
});
