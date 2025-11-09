let toplistaE = []
let toplistaH = []

let palya

function ujEredmeny(nev, ido) {
    if (palya == "easy") {
        toplistaE.push({ nev: nev, ido: ido });
    } else if (palya == "hard") {
        toplistaH.push({ nev: nev, ido: ido });
    }
}

function toplistaMegjelenitese() {
    const toplistaContainer = document.querySelector('#toplista');
    toplistaContainer.innerHTML = '';

    const rendezettLista = rendezettToplista();

    rendezettLista.forEach((eredmeny, index) => {
        const li = document.createElement('li');
        li.innerHTML = `${index + 1}. ${eredmeny.nev} - ` + Math.floor((eredmeny.ido / 60)) + ":" + String((eredmeny.ido % 60)).padStart(2, '0');
        toplistaContainer.appendChild(li);
    });
}

function rendezettToplista() {
    if (palya == "easy") {
        return toplistaE.sort((a, b) => a.ido - b.ido);
    } else if (palya == "hard") {
        return toplistaH.sort((a, b) => a.ido - b.ido);
    }
}



function easyselect() {
    document.querySelector('#easy').classList.add('activeb')
    document.querySelector('#hard').classList.remove('activeb')
}



function hardselect() {
    document.querySelector('#hard').classList.add('activeb')
    document.querySelector('#easy').classList.remove('activeb')
}

let randomMap

function inditas() {
    ido = 0
    korbeE = false
    document.querySelector('#game').addEventListener('click', lehelyez, false)
    nev = document.querySelector("#name").value;
    if (nev != "") {
        document.querySelector('#menu').style.display = "none"
        document.querySelector('#jatek').style.display = "contents"
        document.querySelector("#nev").innerHTML = nev;
        szamol()


        if (document.querySelector("#easy").classList.contains("activeb")) {
            palya = "easy"
            let easySrc = [
                [
                    ["Üres", "HegyL", "Üres", "Üres", "Oázis"],
                    ["Üres", "Üres", "Üres", "HidV", "Oázis"],
                    ["HidV", "Üres", "HegyU", "Üres", "Üres"],
                    ["Üres", "Üres", "Üres", "Oázis", "Üres"],
                    ["Üres", "Üres", "HegyR", "Üres", "Üres"],
                ],
                [
                    ["Oázis", "Üres", "HidH", "Üres", "Üres"],
                    ["Üres", "HegyU", "Üres", "Üres", "HegyU"],
                    ["HidV", "Oázis", "HegyR", "Üres", "Üres"],
                    ["Üres", "Üres", "Üres", "Oázis", "Üres"],
                    ["Üres", "Üres", "Üres", "Üres", "Üres"],
                ], [
                    ["Üres", "Üres", "HidH", "Üres", "Üres"],
                    ["Üres", "Üres", "Üres", "Üres", "HidV"],
                    ["Üres", "HegyU", "HidV", "Üres", "Üres"],
                    ["Üres", "Oázis", "Üres", "Üres", "Üres"],
                    ["Üres", "HidH", "Üres", "Üres", "HegyU"],
                ],
                [
                    ["Üres", "Üres", "Üres", "HidH", "Üres"],
                    ["Üres", "Üres", "Üres", "Üres", "Üres"],
                    ["HidV", "Üres", "HegyL", "Üres", "HegyL"],
                    ["Üres", "Üres", "Üres", "Üres", "Üres"],
                    ["Üres", "Üres", "Oázis", "HegyR", "Üres"],
                ],
                [
                    ["Üres", "Üres", "HidH", "Üres", "Üres"],
                    ["Üres", "Hegy", "Üres", "Üres", "Üres"],
                    ["HidV", "Üres", "Üres", "HegyR", "Üres"],
                    ["Üres", "Üres", "HidV", "Oázis", "Üres"],
                    ["Üres", "HegyU", "Üres", "Üres", "Üres"],
                ]]
            randomMap = easySrc[Math.floor(Math.random() * 5)]
            const rows = 5;
            const cols = 5;

            let game = document.querySelector("#game")

            for (let i = 1; i <= rows; i++) {
                let tr = document.createElement("tr")
                for (let j = 1; j <= cols; j++) {
                    let td = document.createElement("td")
                    td.id = `t${i}${j}`
                    if (randomMap[i - 1][j - 1] == "Üres") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/empty.png)"
                    } else if (randomMap[i - 1][j - 1] == "HegyL") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/mountainL.png)"
                    } else if (randomMap[i - 1][j - 1] == "HegyR") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/mountainR.png)"
                    } else if (randomMap[i - 1][j - 1] == "Hegy") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/mountain.png)"
                    } else if (randomMap[i - 1][j - 1] == "HegyU") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/mountainU.png)"
                    } else if (randomMap[i - 1][j - 1] == "HidH") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/BridgeH.png)"
                    } else if (randomMap[i - 1][j - 1] == "HidV") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/BridgeV.png)"
                    } else if (randomMap[i - 1][j - 1] == "Oázis") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/oasis.png)"
                    }

                    td.style.width = "20%"
                    td.style.height = "20%"

                    tr.appendChild(td)
                }
                game.appendChild(tr)
            }

        } else {
            palya = "hard"
            let hardSrc = [
                [["Üres", "HegyL", "Oázis", "Oázis", "Üres", "HidH", "Üres"],
                ["HidV", "Üres", "Üres", "Üres", "Üres", "Üres", "Üres"],
                ["Üres", "Üres", "HidV", "Üres", "Üres", "Üres", "Üres"],
                ["Üres", "Üres", "Üres", "HegyR", "Üres", "Üres", "Üres"],
                ["HegyR", "Üres", "HegyL", "Üres", "HidH", "Üres", "Oázis"],
                ["Üres", "Üres", "Üres", "Üres", "Üres", "Üres", "Üres"],
                ["Üres", "Üres", "Üres", "HidH", "Üres", "Üres", "Üres"]],

                [["Üres", "Üres", "Oázis", "Üres", "Üres", "Üres", "Üres"],
                ["HidV", "Üres", "HidH", "Üres", "Üres", "HegyU", "Üres"],
                ["Üres", "Üres", "HidH", "Üres", "Üres", "Üres", "HidV"],
                ["Hegy", "Üres", "Üres", "Üres", "Üres", "Üres", "Üres"],
                ["Üres", "Oázis", "Üres", "HegyL", "Üres", "Üres", "Üres"],
                ["Üres", "Hegy", "Üres", "Üres", "Üres", "Üres", "Üres"],
                ["Üres", "Üres", "Oázis", "Üres", "Üres", "Üres", "Üres"]],

                [["Üres", "Üres", "HidH", "Üres", "Üres", "Üres", "Üres"],
                ["Üres", "Üres", "Üres", "Üres", "Üres", "Üres", "HidV"],
                ["Oázis", "Üres", "HegyR", "Üres", "Üres", "Üres", "Üres"],
                ["Üres", "Üres", "Üres", "Üres", "Üres", "Üres", "Üres"],
                ["Üres", "Oázis", "HegyR", "Üres", "HidH", "Üres", "Üres"],
                ["HidV", "Üres", "Üres", "Üres", "Üres", "HegyL", "Üres"],
                ["Üres", "Üres", "Oázis", "HegyR", "Üres", "Üres", "Üres"]],

                [["Üres", "Üres", "Üres", "Üres", "Üres", "Üres", "Üres"],
                ["Üres", "Üres", "Üres", "HidV", "Üres", "HegyU", "Üres"],
                ["Üres", "Üres", "HegyR", "Üres", "Üres", "Üres", "Üres"],
                ["Üres", "HidH", "Üres", "Oázis", "Üres", "HidH", "Üres"],
                ["Üres", "Üres", "HegyU", "Üres", "HegyL", "Üres", "Üres"],
                ["HidV", "Üres", "Üres", "Üres", "Üres", "HegyR", "Üres"],
                ["Üres", "Üres", "Üres", "Üres", "Üres", "Üres", "Üres"]],

                [["Üres", "Üres", "Üres", "Üres", "Üres", "Üres", "Üres"],
                ["Üres", "Üres", "Üres", "Üres", "Üres", "Hegy", "Üres"],
                ["Üres", "HidH", "HidH", "Üres", "HegyL", "Üres", "Üres"],
                ["Üres", "Üres", "Üres", "Üres", "Üres", "Üres", "Üres"],
                ["Üres", "Üres", "Hegy", "Üres", "Oázis", "Üres", "Üres"],
                ["Üres", "HegyU", "Üres", "HidV", "Üres", "Üres", "Üres"],
                ["Üres", "Üres", "Üres", "Üres", "Üres", "Üres", "Üres"]],

            ]
            randomMap = hardSrc[Math.floor(Math.random() * 5)]
            const rows = 7;
            const cols = 7;

            let game = document.querySelector("#game")

            for (let i = 1; i <= rows; i++) {
                let tr = document.createElement("tr")
                for (let j = 1; j <= cols; j++) {
                    let td = document.createElement("td")
                    td.id = `t${i}${j}`

                    if (randomMap[i - 1][j - 1] == "Üres") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/empty.png)"
                    } else if (randomMap[i - 1][j - 1] == "HegyL") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/mountainL.png)"
                    } else if (randomMap[i - 1][j - 1] == "HegyR") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/mountainR.png)"
                    } else if (randomMap[i - 1][j - 1] == "Hegy") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/mountain.png)"
                    } else if (randomMap[i - 1][j - 1] == "HegyU") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/mountainU.png)"
                    } else if (randomMap[i - 1][j - 1] == "HidH") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/BridgeH.png)"
                    } else if (randomMap[i - 1][j - 1] == "HidV") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/BridgeV.png)"
                    } else if (randomMap[i - 1][j - 1] == "Oázis") {
                        td.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/oasis.png)"
                    }

                    td.style.width = "13%"
                    td.style.height = "11%"

                    tr.appendChild(td)
                }
                game.appendChild(tr)
            }
        }
    } else {
        document.querySelector("#name").disabled = true
        showModal()
    }
}

let korbeE = false

function lehelyez(e) {
    id = e.target.id
    if (id != "game") {
        i = parseInt(id.charAt(1))
        j = parseInt(id.charAt(2))
        if (randomMap[i - 1][j - 1] == "Oázis") {
            return 0
        } else if (randomMap[i - 1][j - 1] == "HidH") {
            e.target.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/bridge_railH.png)"
        } else if (randomMap[i - 1][j - 1] == "HidV") {
            e.target.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/bridge_railV.png)"
        } else if (randomMap[i - 1][j - 1] == "HegyR") {
            e.target.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/mountain_railR.png)"
        } else if (randomMap[i - 1][j - 1] == "HegyL") {
            e.target.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/mountain_railL.png)"
        } else if (randomMap[i - 1][j - 1] == "HegyU") {
            e.target.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/mountain_railU.png)"
        } else if (randomMap[i - 1][j - 1] == "Hegy") {
            e.target.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/mountain_rail.png)"
        } else if (randomMap[i - 1][j - 1] == "Üres") {
            e.target.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/straight_rail.png)"
        } else {
            const extractedUrl = e.target.style.backgroundImage.slice(5, -2);

            if (extractedUrl == "./kiindulo_hun/pics/tiles/curve_railU.png") {
                e.target.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/straight_rail.png)"
            } else if (extractedUrl == "./kiindulo_hun/pics/tiles/straight_rail.png") {
                e.target.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/straight_railH.png)"
            } else if (extractedUrl == "./kiindulo_hun/pics/tiles/straight_railH.png") {
                e.target.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/curve_rail.png)"
            } else if (extractedUrl == "./kiindulo_hun/pics/tiles/curve_rail.png") {
                e.target.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/curve_railL.png)"
            } else if (extractedUrl == "./kiindulo_hun/pics/tiles/curve_railL.png") {
                e.target.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/curve_railR.png)"
            } else if (extractedUrl == "./kiindulo_hun/pics/tiles/curve_railR.png") {
                e.target.style.backgroundImage = "url(./kiindulo_hun/pics/tiles/curve_railU.png)"
            }
        }

        randomMap[i - 1][j - 1] = "Sin"
        if (vege()) {
            if (palya == "easy") {
                ujEredmeny(document.querySelector("#name").value, ido)
            } else if (palya == "hard") {
                ujEredmeny(document.querySelector("#name").value, ido)
            }
            toplistaMegjelenitese()
            document.querySelector('#vegido').innerHTML = "Megoldásra szánt idő: " + Math.floor((ido / 60)) + ":" + String((ido % 60)).padStart(2, '0');
            clearTimeout(timerId)
            document.querySelector("#vege-kepernyo").style.display = "block"
            document.querySelector('#game').removeEventListener('click', lehelyez, false)
        }
    }
}

function vege() {

    let bejartae = [];
    for (let i = 0; i < randomMap.length; i++) {
        for (let j = 0; j < randomMap.length; j++) {
            if (randomMap[i][j] != "Oázis" && randomMap[i][j] != "Sin") {
                return false;
            }
        }
    }

    for (let i = 0; i < randomMap.length; i++) {
        bejartae[i] = []
        for (let j = 0; j < randomMap.length; j++) {
            if (randomMap[i][j] == "Oázis") {
                bejartae[i][j] = true;
            } else {
                bejartae[i][j] = false
            }
        }
    }

    let bejarhatoe = true;
    let prevI = -1;
    let prevJ = -1
    let i = 1;
    let j = 1;
    while (randomMap[i - 1][j - 1] == "Oázis") {
        if (j < randomMap.length) {
            j++
        } else {
            j = 0;
            i++
        }
    }
    let kezdoI = i;
    let kezdoJ = j;
    let jelenlegi = document.querySelector(`#t${i}${j}`)


    do {

        const extractedUrl = jelenlegi.style.backgroundImage.slice(5, -2);
        if (extractedUrl == "./kiindulo_hun/pics/tiles/curve_railU.png" || extractedUrl == "./kiindulo_hun/pics/tiles/mountain_railU.png") {
            if (i != 1 && j != 1) {
                bejartae[i - 1][j - 1] = true;
                if (prevJ < j) {
                    let fel = document.querySelector(`#t${i - 1}${j}`).style.backgroundImage.slice(5, -2)
                    if (
                        (fel == "./kiindulo_hun/pics/tiles/bridge_railV.png" ||
                            fel == "./kiindulo_hun/pics/tiles/curve_railL.png" ||
                            fel == "./kiindulo_hun/pics/tiles/curve_rail.png" ||
                            fel == "./kiindulo_hun/pics/tiles/mountain_rail.png" ||
                            fel == "./kiindulo_hun/pics/tiles/mountain_railL.png" ||
                            fel == "./kiindulo_hun/pics/tiles/straight_rail.png")
                    ) {
                        prevI = i;
                        prevJ = j;
                        i--;
                        jelenlegi = document.querySelector(`#t${i}${j}`)
                        bejarhatoe = true;
                    } else {
                        bejarhatoe = false;
                    }
                } else if (prevI < i) {
                    let balra = document.querySelector(`#t${i}${j - 1}`).style.backgroundImage.slice(5, -2)
                    if (
                        (balra == "./kiindulo_hun/pics/tiles/bridge_railH.png" ||
                            balra == "./kiindulo_hun/pics/tiles/curve_rail.png" ||
                            balra == "./kiindulo_hun/pics/tiles/curve_railR.png" ||
                            balra == "./kiindulo_hun/pics/tiles/mountain_rail.png" ||
                            balra == "./kiindulo_hun/pics/tiles/mountain_railR.png" ||
                            balra == "./kiindulo_hun/pics/tiles/straight_railH.png")
                    ) {
                        prevI = i;
                        prevJ = j;
                        j--;
                        jelenlegi = document.querySelector(`#t${i}${j}`)
                        bejarhatoe = true;
                    } else {
                        bejarhatoe = false;
                    }
                }
            } else {

                return false
            }


        } else if (extractedUrl == "./kiindulo_hun/pics/tiles/straight_rail.png" || extractedUrl == "./kiindulo_hun/pics/tiles/bridge_railV.png") {

            if (i != 1 && i != randomMap.length) {
                bejartae[i - 1][j - 1] = true;
                if (prevI < i) {
                    let le = document.querySelector(`#t${i + 1}${j}`).style.backgroundImage.slice(5, -2)
                    if ((
                        le == "./kiindulo_hun/pics/tiles/bridge_railV.png" ||
                        le == "./kiindulo_hun/pics/tiles/curve_railR.png" ||
                        le == "./kiindulo_hun/pics/tiles/curve_railU.png" ||
                        le == "./kiindulo_hun/pics/tiles/mountain_railR.png" ||
                        le == "./kiindulo_hun/pics/tiles/mountain_railU.png" ||
                        le == "./kiindulo_hun/pics/tiles/straight_rail.png"
                    )
                    ) {
                        prevI = i;
                        prevJ = j;
                        i++;
                        jelenlegi = document.querySelector(`#t${i}${j}`)
                        bejarhatoe = true;
                    } else {
                        bejarhatoe = false;
                    }
                } else if (prevI > i) {
                    let fel = document.querySelector(`#t${i - 1}${j}`).style.backgroundImage.slice(5, -2)
                    if (
                        (
                            fel == "./kiindulo_hun/pics/tiles/bridge_railV.png" ||
                            fel == "./kiindulo_hun/pics/tiles/curve_rail.png" ||
                            fel == "./kiindulo_hun/pics/tiles/curve_railL.png" ||
                            fel == "./kiindulo_hun/pics/tiles/mountain_rail.png" ||
                            fel == "./kiindulo_hun/pics/tiles/mountain_railL.png" ||
                            fel == "./kiindulo_hun/pics/tiles/straight_rail.png"
                        )
                    ) {
                        prevI = i;
                        prevJ = j;
                        i--;
                        jelenlegi = document.querySelector(`#t${i}${j}`)
                        bejarhatoe = true;
                    } else {
                        bejarhatoe = false;
                    }
                }
            } else {

                return false
            }


        } else if (extractedUrl == "./kiindulo_hun/pics/tiles/straight_railH.png" || extractedUrl == "./kiindulo_hun/pics/tiles/bridge_railH.png") {

            if (prevI == -1) {
                return false
            } else {
                if (j != 1 && j != randomMap.length) {
                    bejartae[i - 1][j - 1] = true;
                    if (prevJ > j) {

                        let balra = document.querySelector(`#t${i}${j - 1}`).style.backgroundImage.slice(5, -2)
                        if (
                            (
                                balra == "./kiindulo_hun/pics/tiles/bridge_railH.png" ||
                                balra == "./kiindulo_hun/pics/tiles/curve_rail.png" ||
                                balra == "./kiindulo_hun/pics/tiles/curve_railR.png" ||
                                balra == "./kiindulo_hun/pics/tiles/mountain_rail.png" ||
                                balra == "./kiindulo_hun/pics/tiles/mountain_railR.png" ||
                                balra == "./kiindulo_hun/pics/tiles/straight_railH.png"
                            )
                        ) {
                            prevI = i;
                            prevJ = j;
                            j--;
                            jelenlegi = document.querySelector(`#t${i}${j}`)
                            bejarhatoe = true;
                        } else {
                            bejarhatoe = false;
                        }
                    } else if (prevJ < j) {

                        let jobbra = document.querySelector(`#t${i}${j + 1}`).style.backgroundImage.slice(5, -2)
                        if (
                            (
                                jobbra == "./kiindulo_hun/pics/tiles/bridge_railH.png" ||
                                jobbra == "./kiindulo_hun/pics/tiles/curve_railL.png" ||
                                jobbra == "./kiindulo_hun/pics/tiles/curve_railU.png" ||
                                jobbra == "./kiindulo_hun/pics/tiles/mountain_railL.png" ||
                                jobbra == "./kiindulo_hun/pics/tiles/mountain_railU.png" ||
                                jobbra == "./kiindulo_hun/pics/tiles/straight_railH.png")
                        ) {
                            prevI = i;
                            prevJ = j;
                            j++;
                            jelenlegi = document.querySelector(`#t${i}${j}`)
                            bejarhatoe = true;
                        } else {
                            bejarhatoe = false;
                        }
                    }
                } else {
                    return false
                }
            }

        } else if (extractedUrl == "./kiindulo_hun/pics/tiles/curve_rail.png" || extractedUrl == "./kiindulo_hun/pics/tiles/mountain_rail.png") {

            if (prevI == -1) {
                prevI = i;
                prevJ = j
                if (i != randomMap.length && j != randomMap.length) {
                    let jobbra = document.querySelector(`#t${i}${j + 1}`).style.backgroundImage.slice(5, -2)
                    bejartae[i - 1][j - 1] = true;
                    if ((
                        jobbra == "./kiindulo_hun/pics/tiles/bridge_railH.png" ||
                        jobbra == "./kiindulo_hun/pics/tiles/curve_railL.png" ||
                        jobbra == "./kiindulo_hun/pics/tiles/curve_railU.png" ||
                        jobbra == "./kiindulo_hun/pics/tiles/mountain_railL.png" ||
                        jobbra == "./kiindulo_hun/pics/tiles/mountain_railU.png" ||
                        jobbra == "./kiindulo_hun/pics/tiles/straight_railH.png"
                    )) {
                        prevI = i;
                        prevJ = j;
                        j++;
                        jelenlegi = document.querySelector(`#t${i}${j}`)
                        bejarhatoe = true;
                    } else {
                        bejarhatoe = false;
                    }
                } else {
                    return false
                }
            } else {
                if (i != randomMap.length && j != randomMap.length) {
                    if (prevI > i) {
                        bejartae[i - 1][j - 1] = true;
                        let jobbra = document.querySelector(`#t${i}${j + 1}`).style.backgroundImage.slice(5, -2)
                        if ((
                            jobbra == "./kiindulo_hun/pics/tiles/bridge_railH.png" ||
                            jobbra == "./kiindulo_hun/pics/tiles/curve_railL.png" ||
                            jobbra == "./kiindulo_hun/pics/tiles/curve_railU.png" ||
                            jobbra == "./kiindulo_hun/pics/tiles/mountain_railL.png" ||
                            jobbra == "./kiindulo_hun/pics/tiles/mountain_railU.png" ||
                            jobbra == "./kiindulo_hun/pics/tiles/straight_railH.png"
                        )) {
                            prevI = i;
                            prevJ = j;
                            j++;
                            jelenlegi = document.querySelector(`#t${i}${j}`)
                            bejarhatoe = true;
                        } else {
                            bejarhatoe = false;
                        }

                    } else if (prevJ > j) {
                        let le = document.querySelector(`#t${i + 1}${j}`).style.backgroundImage.slice(5, -2)
                        bejartae[i - 1][j - 1] = true;
                        if (
                            (
                                le == "./kiindulo_hun/pics/tiles/bridge_railV.png" ||
                                le == "./kiindulo_hun/pics/tiles/curve_railR.png" ||
                                le == "./kiindulo_hun/pics/tiles/curve_railU.png" ||
                                le == "./kiindulo_hun/pics/tiles/mountain_railR.png" ||
                                le == "./kiindulo_hun/pics/tiles/mountain_railU.png" ||
                                le == "./kiindulo_hun/pics/tiles/straight_rail.png"
                            )) {
                            prevI = i;
                            prevJ = j;
                            i++;
                            jelenlegi = document.querySelector(`#t${i}${j}`)
                            bejarhatoe = true;
                        } else {
                            bejarhatoe = false;
                        }
                    }
                } else {
                    return false
                }
            }

        } else if (extractedUrl == "./kiindulo_hun/pics/tiles/curve_railL.png" || extractedUrl == "./kiindulo_hun/pics/tiles/mountain_railL.png") {

            if (prevI == -1) {
                return false
            } else {
                if (i != randomMap.length && j != 1) {
                    bejartae[i - 1][j - 1] = true;
                    if (prevJ < j) {
                        let le = document.querySelector(`#t${i + 1}${j}`).style.backgroundImage.slice(5, -2)
                        if (
                            (
                                le == "./kiindulo_hun/pics/tiles/bridge_railV.png" ||
                                le == "./kiindulo_hun/pics/tiles/curve_railR.png" ||
                                le == "./kiindulo_hun/pics/tiles/curve_railU.png" ||
                                le == "./kiindulo_hun/pics/tiles/mountain_railR.png" ||
                                le == "./kiindulo_hun/pics/tiles/mountain_railU.png" ||
                                le == "./kiindulo_hun/pics/tiles/straight_rail.png")
                        ) {
                            prevI = i;
                            prevJ = j;
                            i++;
                            jelenlegi = document.querySelector(`#t${i}${j}`)
                            bejarhatoe = true;
                        } else {
                            bejarhatoe = false;
                        }
                    } else if (prevI > i) {
                        let balra = document.querySelector(`#t${i}${j - 1}`).style.backgroundImage.slice(5, -2)
                        if (
                            (balra == "./kiindulo_hun/pics/tiles/bridge_railH.png" ||
                                balra == "./kiindulo_hun/pics/tiles/curve_rail.png" ||
                                balra == "./kiindulo_hun/pics/tiles/curve_railR.png" ||
                                balra == "./kiindulo_hun/pics/tiles/mountain_rail.png" ||
                                balra == "./kiindulo_hun/pics/tiles/mountain_railR.png" ||
                                balra == "./kiindulo_hun/pics/tiles/straight_railH.png"
                            )
                        ) {
                            prevI = i;
                            prevJ = j;
                            j--;
                            jelenlegi = document.querySelector(`#t${i}${j}`)
                            bejarhatoe = true;
                        } else {
                            bejarhatoe = false;
                        }
                    }
                } else {
                    return false
                }
            }

        } else if (extractedUrl == "./kiindulo_hun/pics/tiles/curve_railR.png" || extractedUrl == "./kiindulo_hun/pics/tiles/mountain_railR.png") {

            if (i != 1 && j != randomMap.length) {
                bejartae[i - 1][j - 1] = true;
                if (prevI < i) {
                    let jobbra = document.querySelector(`#t${i}${j + 1}`).style.backgroundImage.slice(5, -2)
                    if (
                        (
                            jobbra == "./kiindulo_hun/pics/tiles/bridge_railH.png" ||
                            jobbra == "./kiindulo_hun/pics/tiles/curve_railL.png" ||
                            jobbra == "./kiindulo_hun/pics/tiles/curve_railU.png" ||
                            jobbra == "./kiindulo_hun/pics/tiles/mountain_railL.png" ||
                            jobbra == "./kiindulo_hun/pics/tiles/mountain_railU.png" ||
                            jobbra == "./kiindulo_hun/pics/tiles/straight_railH.png"
                        )
                    ) {
                        prevI = i;
                        prevJ = j;
                        j++;
                        jelenlegi = document.querySelector(`#t${i}${j}`)
                        bejarhatoe = true;
                    } else {
                        bejarhatoe = false;
                    }
                } else if (prevJ > j) {
                    let fel = document.querySelector(`#t${i - 1}${j}`).style.backgroundImage.slice(5, -2)
                    if ((
                        fel == "./kiindulo_hun/pics/tiles/bridge_railV.png" ||
                        fel == "./kiindulo_hun/pics/tiles/curve_rail.png" ||
                        fel == "./kiindulo_hun/pics/tiles/curve_railL.png" ||
                        fel == "./kiindulo_hun/pics/tiles/mountain_rail.png" ||
                        fel == "./kiindulo_hun/pics/tiles/mountain_railL.png" ||
                        fel == "./kiindulo_hun/pics/tiles/straight_rail.png"
                    )
                    ) {
                        prevI = i;
                        prevJ = j;
                        i--;
                        jelenlegi = document.querySelector(`#t${i}${j}`)
                        bejarhatoe = true;
                    } else {
                        bejarhatoe = false;
                    }
                }
            } else {
                return false
            }

        } else {
            return false
        }

    } while ((kezdoI != i || kezdoJ != j) && bejarhatoe);



    if (bejarhatoe) {
        if (bejartae.some(innerArray => innerArray.includes(false))) {
            return false;
        }
        return true
    }
    return false
}



function showModal() {
    document.querySelector("#customModal").style.display = "block";
}



function closeModal() {
    document.querySelector("#customModal").style.display = "none";
    document.querySelector("#name").disabled = false
}



let ido = 0
let timerId
function szamol() {
    document.querySelector('#time').innerHTML = Math.floor((ido / 60)) + ":" + String((ido % 60)).padStart(2, '0');;
    ido++;
    timerId = setTimeout(szamol, 1000);
}



function backToMenu() {
    document.body.style.background = "url('./kiindulo_hun/pics/screens/bg.png')"
    document.body.style.backgroundSize = "85%"
    document.querySelector("#menu").style.display = "contents";
    document.querySelector('#leiras').style.display = "none";
    document.querySelector('#jatek').style.display = "none";
    let parentElement = document.querySelector('#game')
    while (parentElement.firstChild) {
        parentElement.removeChild(parentElement.firstChild);
    }
    
    document.querySelector('#vege-kepernyo').style.display = "none";
}



function leiras() {
    document.body.style.background = "#F8FAF0"
    document.querySelector("#menu").style.display = "none";
    document.querySelector('#leiras').style.display = "contents";
}



function saveTopScores() {
    localStorage.setItem("toplistE", JSON.stringify(toplistaE));
    localStorage.setItem("toplistH", JSON.stringify(toplistaH));
}

function loadETopScores() {
    toplistaE = localStorage.getItem("toplistE");
    if (toplistaE) {
        return JSON.parse(toplistaE);
    }
    return [];
}

function loadHTopScores() {
    toplistaH = localStorage.getItem("toplistH");
    if (toplistaH) {
        return JSON.parse(toplistaH);
    }
    return [];
}





function init() {
    document.querySelector('#easy').addEventListener('click', easyselect, false)
    document.querySelector('#hard').addEventListener('click', hardselect, false)
    document.querySelector('#indit').addEventListener('click', inditas, false)
    document.querySelector('#bezar').addEventListener('click', closeModal, false)
    document.querySelector('#back').addEventListener('click', backToMenu, false)
    document.querySelector('#leir').addEventListener('click', leiras, false)
    document.querySelector('#vissza').addEventListener('click', backToMenu, false)
    toplistaE = loadETopScores()
    toplistaH = loadHTopScores()
}

window.addEventListener('load', init, false)

window.addEventListener("beforeunload", saveTopScores, false)