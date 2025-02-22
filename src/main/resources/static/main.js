let SELF = null;
let PLAYERS = [];
let CURRENT_STICH = null;
let PREV_STICH = null;
let CURRENT_TURN = "LINKS"
const DEBUG = true;
let PERIODIC_CALL; // This will periodically check if the socket is still alive and reconnect if needed.
let SOCKET; // This will receive updates from the server.





/*
 * Functions for API-Communication
 */

async function api_get(url) {
    const response = await fetch(url, {
        method: "GET",
        headers: {"Content-Type": "application/json",},
        cache: "no-cache",
    });
    if (response.status !== 200) {
        console.log(response);
        return null;
    }
    const ret = await response.json();
    if (DEBUG) {
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
    if (response.status !== 200) {
        console.log(response);
        return null;
    }
    const ret = await response.json();
    if (DEBUG) {
        console.log(ret);
    }
    return ret;
}






/*
 * Game Actions
 */

async function join(player_name) {
    SELF = await api_post_body("/join", {"spielername": player_name});
    document.getElementById("join").style.display = "none";
    document.getElementById("play").style.display = "block";
    document.getElementById("vorbehaltfield").style.display = "block";
    document.getElementById("display_player_name").textContent = SELF.name;
    await refresh()
    await connectSocket()
    PERIODIC_CALL = setInterval(() => {
        if(SOCKET == null) {
            console.log("WebSocket not started or was terminated")
            connectSocket()
        }
    }, 10_000);

}

async function connectSocket() {
    console.log("Connecting WebSocket...")
    SOCKET = new WebSocket("/subscribe");
    SOCKET.addEventListener("message", (evt) => {
        if (DEBUG) console.log("websocket: " + evt.data);
        if (evt.data === "update") {
            refresh()
        }
    })
    console.log("WebSocket connected successfully")
}

async function layCard(pos) {
    if (DEBUG) console.log("CARD_POS " + pos);
    if (SELF.hand.length >= pos) {
        resp = await api_post_body("/putcard", {"token": SELF.sessionToken, "card": SELF.hand[pos]});
        await refresh()
    }
}

async function vorbehalt(vorbehalt) {
    if (DEBUG) console.log(vorbehalt)
    resp = await api_post_body("/putvorbehalt", {"token": SELF.sessionToken, "vorbehalt": vorbehalt});
    await refresh()
}


/**
 * Update UI
 */
async function refresh() {
    function cardOrderCompare(cardA, cardB) {
        cardValues = {
            "KA_9": 101, "KA_K": 102, "KA_10": 103, "KA_A": 104, "KA_B": 201, "KA_D": 301,
            "HE_9": 11, "HE_K": 12, "HE_10": 401, "HE_A": 13, "HE_B": 202, "HE_D": 302,
            "PI_9": 21, "PI_K": 22, "PI_10": 23, "PI_A": 24, "PI_B": 203, "PI_D": 303,
            "KR_9": 31, "KR_K": 32, "KR_10": 33, "KR_A": 34, "KR_B": 204, "KR_D": 304,
        }
        return cardValues[cardB] - cardValues[cardA]
    }
    if (DEBUG) console.log("Refreshing...");
    let resp = await api_post_body("/update", {"token": SELF.sessionToken});
    PLAYERS = resp.spielerListe;
    CURRENT_STICH = resp.aktuellerStich;
    PREV_STICH = resp.letzterStich;
    resp.playerself.hand.sort(cardOrderCompare);
    SELF = resp.playerself
    CURRENT_TURN = resp.currentTurn
    displayPlayerTableInfo();
    displayParty();
    displayTableCards();
    displayHand();
}





/**
 * Display Functions
 */

function displayPlayerTableInfo() {
    const mapping = {"LINKS": "left", "RECHTS": "right", "OBEN": "top", "UNTEN": "bottom"};
    PLAYERS.forEach(p => {
            if (p != null && p.position != null) {
                document.getElementById("player_" + mapping[p.position]).textContent = p.name;
                document.getElementById("points_" + mapping[p.position]).textContent = p.punkte;
                document.getElementById("vorbehalt_" + mapping[p.position]).textContent = p.vorbehalt;
                document.getElementById("player_" + mapping[p.position]).style.color = "#000000";
                if (CURRENT_TURN === p.position) {
                    document.getElementById("player_" + mapping[p.position]).style.color = "#6e0606";
                }
                if (SELF.position === p.position) {
                    document.getElementById("player_" + mapping[p.position]).textContent = "Du";
                }
            }
        }
    )
}

function displayParty() {
    if (SELF.partei === "RE") {
        document.getElementById("display_player_party").textContent = "Re";
    } else if (SELF.partei === "KONTRA") {
        document.getElementById("display_player_party").textContent = "Kontra";
    } else {
        document.getElementById("display_player_party").textContent = "";
    }
}

function displayTableCards() {
    if (DEBUG) {
        console.log("CURRENT_STICH " + CURRENT_STICH)
        console.log("PREV_STICH " + PREV_STICH)
    }
    let L = null;
    let R = null;
    let B = null;
    let T = null;
    if (CURRENT_STICH != null) {
        L = CURRENT_STICH.links;
        R = CURRENT_STICH.rechts;
        B = CURRENT_STICH.unten;
        T = CURRENT_STICH.oben;
        if (PREV_STICH != null && CURRENT_STICH.links == null && CURRENT_STICH.rechts == null && CURRENT_STICH.oben == null && CURRENT_STICH.unten == null){
            L = PREV_STICH.links;
            R = PREV_STICH.rechts;
            B = PREV_STICH.unten;
            T = PREV_STICH.oben;
        }
    }
    if (L != null) displayCard("t_card_left", L); else displayCard("t_card_left", "BLANC");
    if (T != null) displayCard("t_card_top", T); else displayCard("t_card_top", "BLANC");
    if (R != null) displayCard("t_card_right", R); else displayCard("t_card_right", "BLANC");
    if (B != null) displayCard("t_card_bottom", B); else displayCard("t_card_bottom", "BLANC");
}

function displayHand() {
    if (SELF.hand != null) {
        for (let i = 0; i < SELF.hand.length; i++) {
            displayCard("card_" + i, SELF.hand[i]);
        }
        for (let i = SELF.hand.length; i < 12; i++) {
            displayCard("card_" + i, "BLANC");
        }
    }
}

function displayCard(cardName, cardEnum) {
    document.getElementById(cardName + "_KA").style.display = "none";
    document.getElementById(cardName + "_HE").style.display = "none";
    document.getElementById(cardName + "_PI").style.display = "none";
    document.getElementById(cardName + "_KR").style.display = "none";
    document.getElementById(cardName + "_val").textContent = "";
    if (cardEnum === "BLANC") return;
    if (cardEnum.startsWith("KA")) document.getElementById(cardName + "_KA").style.display = "block";
    if (cardEnum.startsWith("HE")) document.getElementById(cardName + "_HE").style.display = "block";
    if (cardEnum.startsWith("PI")) document.getElementById(cardName + "_PI").style.display = "block";
    if (cardEnum.startsWith("KR")) document.getElementById(cardName + "_KR").style.display = "block";
    document.getElementById(cardName + "_val").textContent = cardEnum.split("_")[1];
}
