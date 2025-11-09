function add(){
    document.querySelector("#seats").value = parseInt(document.querySelector("#seats").value)+1
}

function min(){
    if(document.querySelector("#seats").value <= 0){
        
    }else{
        document.querySelector("#seats").value = parseInt(document.querySelector("#seats").value)-1
    }
}

function init() {
    document.querySelector("#pluss").addEventListener('click',add,false);
    document.querySelector("#minus").addEventListener('click',min, false);
}

window.addEventListener('load', init, false);