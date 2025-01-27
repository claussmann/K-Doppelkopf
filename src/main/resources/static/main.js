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
    await refresh()
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
    tmp = await api_get("/player/private/" + SELF.sessionToken);
    tmp.hand.sort(cardOrderCompare)
    SELF = tmp
    updateUI()
}

function cardOrderCompare(cardA, cardB) {
    cardValues = {
        "KA_9": 101, "KA_K": 102, "KA_10": 103, "KA_A": 104, "KA_B": 201, "KA_D": 301,
        "HE_9": 11, "HE_K": 12, "HE_10": 401, "HE_A": 13, "HE_B": 202, "HE_D": 302,
        "PI_9": 21, "PI_K": 22, "PI_10": 23, "PI_A": 24, "PI_B": 203, "PI_D": 303,
        "KR_9": 31, "KR_K": 32, "KR_10": 33, "KR_A": 34, "KR_B": 204, "KR_D": 304,
    }
    return cardValues[cardB] - cardValues[cardA]
}

function updateUI() {
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
    if (SELF != null) {
        document.getElementById("display_player_name").textContent=SELF.name;
        document.getElementById("display_player_party").textContent=SELF.partei;
        for (let i = 0; i < 12; i++){
            document.getElementById("card_" + i + "_KA").style.display="none";
            document.getElementById("card_" + i + "_HE").style.display="none";
            document.getElementById("card_" + i + "_PI").style.display="none";
            document.getElementById("card_" + i + "_KR").style.display="none";
            document.getElementById("card_" + i + "_val").textContent="";
        }
        for (let i = 0; i < SELF.hand.length; i++){
            if (SELF.hand[i].startsWith("KA")) document.getElementById("card_" + i + "_KA").style.display="block";
            if (SELF.hand[i].startsWith("HE")) document.getElementById("card_" + i + "_HE").style.display="block";
            if (SELF.hand[i].startsWith("PI")) document.getElementById("card_" + i + "_PI").style.display="block";
            if (SELF.hand[i].startsWith("KR")) document.getElementById("card_" + i + "_KR").style.display="block";
            document.getElementById("card_" + i + "_val").textContent=SELF.hand[i].split("_")[1];
        }
    }
}