<?php 
    session_start();

    $bejelentkezve = isset($_SESSION['user']);
    $admin = false;
    $foglalasok = readJSON("foglalas.json");
    $regdata = readJSON("regdata.json");

    $dateStart = "";
    $dateEnd = "";
    $mindenjo = true;
    $datumjo = true;

    $dateStartErr = "";
    $dateEndErr = "";

    $szures = false;

    $cars = readJSON("cars.json");

    
    $actualCar = null;
    if($_GET && isset($_GET['id'])){
        foreach ($cars as $car) {
            if($car['id'] == $_GET['id']){
                $actualCar = $car;
            }
        }
    }

    if($bejelentkezve && $_SESSION['user']['fullname'] == "admin"){
        $admin = true; 
    }

    if($_POST){
        if(!empty($_POST['dateStart']) ){
            if(preg_match('/^[1-9][0-9]{3}.[0,1][0-9].[0-3][0-9]$/', $_POST['dateStart'])){
                if(preg_match('/^[1-9][0-9]{3}.0[0-9].[0-3][0-9]$/', $_POST['dateStart'])){
                    if(preg_match('/^[1-9][0-9]{3}.0[0-9].3[0,1]$/', $_POST['dateStart'])){
                        $dateStart = $_POST['dateStart'];
                    }else if(preg_match('/^[1-9][0-9]{3}.0[0-9].[0-2][0-9]$/', $_POST['dateStart'])){
                        $dateStart = $_POST['dateStart'];
                    }else{
                        $mindenjo = false;
                        $dateStartErr = "A nap nem helyes! Maximum 31 napunk van egyes hónapokban!";
                    }
                }else if(preg_match('/^[1-9][0-9]{3}.1[0-2].[0-3][0-9]$/', $_POST['dateStart'])){
                    if(preg_match('/^[1-9][0-9]{3}.1[0-2].3[0,1]$/', $_POST['dateStart'])){
                        $dateStart = $_POST['dateStart'];
                    }else if(preg_match('/^[1-9][0-9]{3}.1[0-2].[0-2][0-9]$/', $_POST['dateStart'])){
                        $dateStart = $_POST['dateStart'];
                    }else{
                        $mindenjo = false;
                        $dateStartErr = "A nap nem helyes! Maximum 31 napunk van egyes hónapokban!";
                    }
                }else{
                    $mindenjo = false;
                    $dateStartErr = "A hónap nem helyes! Csak 12 hónapunk van!";
                }
            }else{
                $mindenjo = false;
                $dateStartErr= "Ilyen formátumban add meg a dátumot: 2024.10.10";
            }
        }else{
            $mindenjo = false;
            $dateStartErr = "Kezdődátumot kötelező megadni!";
        }
        if(!empty($_POST['dateEnd'])){
            if(preg_match('/^[1-9][0-9]{3}.[0,1][0-9].[0-3][0-9]$/', $_POST['dateEnd'])){
                if(preg_match('/^[1-9][0-9]{3}.0[0-9].[0-3][0-9]$/', $_POST['dateEnd'])){
                    if(preg_match('/^[1-9][0-9]{3}.0[0-9].3[0,1]$/', $_POST['dateEnd'])){
                        $dateEnd = $_POST['dateEnd'];
                    }else if(preg_match('/^[1-9][0-9]{3}.0[0-9].[0-2][0-9]$/', $_POST['dateEnd'])){
                        $dateEnd = $_POST['dateEnd'];
                    }else{
                        $mindenjo = false;
                        $dateEndErr = "A nap nem helyes! Maximum 31 napunk van egyes hónapokban!";
                    }
                }else if(preg_match('/^[1-9][0-9]{3}.1[0-2].[0-3][0-9]$/', $_POST['dateEnd'])){
                    if(preg_match('/^[1-9][0-9]{3}.1[0-2].3[0,1]$/', $_POST['dateEnd'])){
                        $dateEnd = $_POST['dateEnd'];
                    }else if(preg_match('/^[1-9][0-9]{3}.1[0-2].[0-2][0-9]$/', $_POST['dateEnd'])){
                        $dateEnd = $_POST['dateEnd'];
                    }else{
                        $mindenjo = false;
                        $dateEndErr = "A nap nem helyes! Maximum 31 napunk van egyes hónapokban!";
                    }
                }else{
                    $mindenjo = false;
                    $dateEndErr = "A hónap nem helyes! Csak 12 hónapunk van!";
                }
            }else{
                $mindenjo = false;
                $dateEndErr= "Ilyen formátumban add meg a dátumot: 2024.10.10";
            }
        }else{
            $mindenjo = false;
            $dateEndErr = "Végdátumot kötelező megadni!";
        }
        if(!empty($dateEnd) && !empty($dateStart) && $dateEnd < $dateStart){
            $dateEnd = "";
            $dateStart = "";
            $dateEndErr = "A kezdő dátum nem lehet később mint a vége dátum";
            $dateStartErr = "A kezdő dátum nem lehet később mint a vége dátum";
        }
        foreach ($foglalasok as $foglalas) {
            if($foglalas['carId'] == $_GET['id']){
                if (!empty($dateStart) && $foglalas['fromDate'] >= $dateStart && $foglalas['toDate'] <= $dateEnd) {
                    $datumjo = false;
                }else if(!empty($dateStart) && $foglalas['fromDate'] >= $dateStart && $foglalas['toDate'] > $dateEnd && $foglalas['fromDate'] < $dateEnd){
                    $datumjo = false;
                }else if(!empty($dateStart) && $foglalas['fromDate'] < $dateStart && $foglalas['toDate'] <= $dateEnd && $foglalas['toDate'] > $dateStart){
                    $datumjo = false;
                }
            }
        }

        if($mindenjo && $datumjo){
            $elem = [
                "id" => end($foglalasok)['id']+1,
                "carId" => $actualCar['id'],
                "brand"=> $actualCar['brand'],
                "model" => $actualCar['model'],
                "transmission" => $actualCar['transmission'],
                "passengers" => $actualCar['passengers'],
                "fromDate"=> $dateStart,
                "toDate"=> $dateEnd,
                "image" => $actualCar['image']
            ];
            array_push($_SESSION['user']['reservations'],$elem);
            $newregData = [];
            foreach ($regdata as $user) {
                if($user['fullname'] == $_SESSION['user']['fullname']){
                    array_push($newregData,$_SESSION['user']);
                }else{
                    array_push($newregData,$user);
                }
            }
            writeJSON("regdata.json",$newregData);
            array_push($foglalasok,$elem);
            writeJSON("foglalas.json",$foglalasok);
        }
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
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@100&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="style.css">
    <title>iKarRental</title>
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
        <?php if(!$_POST || !$mindenjo):?>
        <div class="container">
            <div class="row">
                <div class="col-md-6"></div>
                <div class="col-md-6 name">
                    <p><?= $actualCar['brand']?> <b><?= $actualCar['model']?></b></p>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <img src="<?= $actualCar['image']?>" alt="" id="pic">
                </div>
                <div class="col-md-6">
                    <div class="content">
                    <div class="row data">
                            <div class="col-md-12">
                                <div class="row">
                                    <div class="col-md-6 infocell">
                                        <p class="info">Üzemanyag: <?= $actualCar['fuel_type'] == "Petrol" ? "Benzin" : "Dízel" ?></p>
                                    </div>
                                    <div class="col-md-6 infocell">
                                        <p class="info">Gyártási év: <?= $actualCar['year'] ?></p>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 infocell">
                                        <p class="info">Váltó: <?= $actualCar['transmission'] == "Manual" ? "Manuális" : "Automata" ?></p>
                                    </div>
                                    <div class="col-md-6 infocell">
                                        <p class="info">Férőhelyek száma:: <?= $actualCar['passengers'] ?></p>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12 dayPrice">
                                        <p><?= number_format($actualCar['daily_price_huf'],0,",",".")?> Ft<sub class="notBold">/nap</sub></p>
                                    </div>
                                </div>
                            </div>
                    </div>
                    <div class="row">
                        <?php if(isset($_SESSION['user'])):?>
                        <form action="" method="POST">
                            <div class="row">
                                <div class="col-md-8">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <input type="text" id="dateStart" name="dateStart" placeholder="2024.10.10" value="<?= $dateStart?>">
                                            <label for="dateStart">Dátum kezdete</label>
                                            <span class="err"><?= $dateStartErr?></span>
                                        </div>
                                        <div class="col-md-6">
                                            <input type="text" id="dateEnd" name="dateEnd" placeholder="2024.10.25" value="<?= $dateEnd?>">
                                            <label for="dateEnd">Dátum vége</label>
                                            <span class="err"><?= $dateEndErr?></span>
                                        </div>
                                    </div>
                                </div>
                                <br>
                                <div class="col-md-4">
                                    <button type="submit" class="btn btn-warning w-100" id="rent">Lefoglalom</button>
                                </div>
                            </div>
                        </form>
                        <?php endif?>
                        <?php if(!isset($_SESSION['user'])):?>
                            <a href="login.php" class="login">Bejelentkezés</a>
                        <?php endif?>
                    </div> 
                    </div>
                </div>
            </div>
        </div>
        <?php endif?>
        <?php if($_POST && $mindenjo && $datumjo):?>
        <div class="container text-center success">
            <div class="iconSucc">✔</div>
            <h1 class="title">Sikeres foglalás!</h1>
            <p class="message">
                A(z) <b><?= $actualCar['brand']." ".$actualCar['model']?></b> sikeresen lefoglalva <b><?=$dateStart?></b>–<b><?=$dateEnd?></b> intervallumra.<br>
                Foglalásod státuszát a profiloldaladon követheted nyomon.
            </p>
            <br>
            <a href="profile.php" class="button">Profilom</a>
        </div>
        <?php endif?>
        <?php if($_POST && $mindenjo && !$datumjo):?>
        <div class="container text-center error">
            <div class="iconErr">✖</div>
            <h1 class="title">Sikertelen foglalás!</h1>
            <p class="message">
                A(z) <b><?= $actualCar['brand']." ".$actualCar['model']?></b> nem elérhető a megadott <b><?=$dateStart?></b>–<b><?=$dateEnd?></b> intervallumban.<br>
                Próbálj megadni egy másik intervallumot, vagy keress egy másik járművet.
            </p>
            <a href="car.php?id=<?=$actualCar['id']?>" class="button">Vissza a jármű oldalára</a>
        </div>
        <?php endif?>
    </body>
</html>