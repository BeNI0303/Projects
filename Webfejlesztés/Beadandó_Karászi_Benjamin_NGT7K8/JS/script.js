function alternativ(){
    var check = document.getElementById("gyengenlato");
    var body = document.body;
    
    var tortenete = document.getElementById("tortenete");

    var technikak = document.getElementById("technikak");

    var textarea = document.getElementById("reviev");
    if(check != null){ 
        if(check.hasAttribute("checked")){
            body.style.fontSize = "1.6em";

            if(window.outerWidth <= 1010){
                if(technikak != null){
                    technikak.style.fontSize = "0.768em";
                }
                if(textarea != null){
                    textarea.setAttribute("cols",23);
                    textarea.setAttribute("rows",20);
                }
            }else{
                if(technikak != null){
                    technikak.style.fontSize = "1.6em";
                }
                if(textarea != null){
                    textarea.setAttribute("cols",36);
                    textarea.setAttribute("rows",13);
                }
            }
    
            if(tortenete != null){
                tortenete.style.fontSize = "2.5em";
            }
        }else if(!check.hasAttribute("checked")){
            body.style.fontSize = "1.07em";
            if(tortenete != null){
                tortenete.style.fontSize = "3em";
            }
            if(technikak != null){
                technikak.style.fontSize = "1.07em";
            }
        }
    }
}

function check(){
    var check = document.getElementById("gyengenlato");
    check.toggleAttribute("checked");
    alternativ();
}

window.onresize = alternativ;