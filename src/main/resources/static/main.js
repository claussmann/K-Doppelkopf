let SELF = null;
let LEFT = null;
let RIGHT = null;
let BOTTOM = null;
let TOP = null;
const DEBUG = true;
const PERIODIC_CALL_INTERVAL = 5000
let PERIODIC_CALL;
const POS_TO_LABEL = {
    "LINKS" : "left",
    "RECHTS" : "right",
    "OBEN" : "top",
    "UNTEN" : "bottom"
}

async function api_get(url) {
    const response = await fetch(url, {
        method: "GET",
        headers: {"Content-Type": "application/json",},
        cache: "no-cache",
    });
    if(response.status !== 200){
        console.log(response);
        return null;
    }
    const ret = await response.json();
    if(DEBUG){
        console.log(ret);
    }
    return ret;
}

async function api_post_body(url, body) {
    const response = await fetch(url, {
        method: "POST",
        headers: {"Content-Type": "application/json",},
        cache: "no-cache",
        body: JSON.stringify(body),
    });
    if(response.status !== 200){
        console.log(response);
        return null;
    }
    const ret = await response.json();
    if(DEBUG){
        console.log(ret);
    }
    return ret;
}

async function join(player_name) {
    SELF = await api_post_body("/join", {"spielername" : player_name});
    updateUI()
    document.getElementById("join").style.display = "none";
    document.getElementById("play").style.display = "block";
    PERIODIC_CALL = setInterval(refresh, PERIODIC_CALL_INTERVAL);
}

async function refresh() {
    console.log("Refreshing...")
    LEFT = await api_get("/player/links");
    RIGHT = await api_get("/player/rechts");
    TOP = await api_get("/player/oben");
    BOTTOM = await api_get("/player/unten");
    SELF = await api_get("/player/private/" + SELF.sessionToken);
    updateUI()
}

function updateUI() {
    document.getElementById("display_player_name").textContent=SELF.name;
    document.getElementById("display_player_token").textContent=SELF.sessionToken;
    if (LEFT != null) {
        document.getElementById("player_left").textContent = LEFT.name;
        document.getElementById("points_left").textContent = LEFT.punkte;
    }
    if (TOP != null) {
        document.getElementById("player_top").textContent = TOP.name;
        document.getElementById("points_top").textContent = TOP.punkte;
    }
    if (RIGHT != null) {
        document.getElementById("player_right").textContent = RIGHT.name;
        document.getElementById("points_right").textContent = RIGHT.punkte;
    }
    if (BOTTOM != null) {
        document.getElementById("player_bottom").textContent = BOTTOM.name;
        document.getElementById("points_bottom").textContent = BOTTOM.punkte;
    }
}