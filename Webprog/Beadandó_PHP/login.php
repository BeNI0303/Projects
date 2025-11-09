<?php 
    session_start();

    if($_GET){
        unset($_SESSION['user']);
    }

    $password = "";
    $email = "";

    $passwordErr = "";
    $emailErr = "";
    $megtalaltErr = "";
    $megtalaltSuccess = "";

    $mindenjo = true;

    if($_POST){
        if(!empty($_POST['password'])){
            $password = $_POST['password'];
        }else{
            $mindenjo = false;
            $passwordErr = "A jelszót muszáj megadni!";
        }
        if(!empty($_POST['email'])){
            if(filter_var($_POST['email'],FILTER_VALIDATE_EMAIL)){
                $email = $_POST['email'];
            }else{
                $mindenjo = false;
                $emailErr = "Az emailnek ilyen formátumba kell lennie: jakab.gipsz@ikarrental.net";
            }
        }else{
            $mindenjo = false;
            $emailErr = "Az emailt muszáj megadni!";
        }
        if($mindenjo){
            $megtalalt = false;
            $regisztraltak = readJSON("regdata.json");
            foreach ($regisztraltak as $ember) {
                if($ember['email'] == $email){
                    $megtalalt = true;
                    if($ember['password'] == $password){
                        $_SESSION['user'] = $ember;
                        $megtalaltSuccess = "Sikeres bejelentkezés";
                    }else{
                        $passwordErr = "A jelszó nem egyezik!";
                    }
                }
            }
            if(!$megtalalt){
                $megtalaltErr = "Nincs ilyen felhasználó";
            }
        }
    }


    $bejelentkezve = isset($_SESSION['user']);
    $admin = false;

    if($bejelentkezve && $_SESSION['user']['fullname'] == "admin"){
        $admin = true; 
    }
    
    function readJSON($fileName) {
        $jsonText = file_get_contents($fileName);
        $json = json_decode($jsonText, true);
        return $json;
    }
    
    function writeJSON($fileName, $json) {
        $jsonText = json_encode($json, JSON_PRETTY_PRINT);
        file_put_contents($fileName, $jsonText);
    }
?>
<!DOCTYPE html>
<html lang="hu">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Regisztráció</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="reg.css">
    </head>
    <body>
        <nav>
            <div class="logo">
                <a href="index.php">iKarRental</a>
            </div>
            <?php if(!$bejelentkezve):?>
                <nav>
                    <a href="login.php" id="login">Bejelentkezés</a>
                    <a href="register.php" class="reg">Regisztráció</a>
                </nav>
            <?php else:?>
                <nav id="profilenav">
                    <a href="login.php?logout=true" class="logout">Kijelentkezés</a>
                    <a href="profile.php" id="profile"><img id="profilep" src="<?=$_SESSION['user']['img']?>" alt=""></a>
                </nav>
            <?php endif?>
        </nav>
        <div class="container">
            <div class="row">
                <div class="col-md-12 text-center">
                    <p class="header">Bejelentkezés</p>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <p id="success"><?= $megtalaltSuccess?></p>
                    <form action="login.php" method="POST">
                        <div class="mb-6">
                            <label for="email" class="form-label">E-mail cím</label>
                            <input type="email" name="email" class="input" id="email" placeholder="jakab.gipsz@ikarrental.net" value="<?=$email?>">
                            <span class="err"><?= $emailErr?></span>
                        </div>
                        <div class="mb-6">
                            <label for="password" class="form-label">Jelszó</label>
                            <input type="password" name="password" class="input" id="password" placeholder="********">
                            <span class="err"><?= $passwordErr?></span>
                        </div>
                        <br>
                        <button type="submit" class="btn btn-warning btn-lg fw-semibold">Bejelentkezés</button>
                    </form>
                    <span class="err"><?= $megtalaltErr?></span>
                </div>
            </div>
        </div>
    </body>
</html>

