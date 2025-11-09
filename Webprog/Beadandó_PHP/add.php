<?php 
    session_start();

    $bejelentkezve = isset($_SESSION['user']);
    $admin = false;

    if($bejelentkezve && $_SESSION['user']['fullname'] == "admin"){
        $admin = true; 
    }

    $cars = readJSON("cars.json");

    $brand = "";
    $model = "";
    $year = "";
    $gearbox = "";
    $fuel_type = "";
    $passengers = "";
    $daily_price_huf = "";
    $image = "";
    $mindenjo = true;

    $brandErr = "";
    $modelErr = "";
    $yearErr = "";
    $gearboxErr = "";
    $fuel_typeErr = "";
    $passengersErr = "";
    $daily_price_hufErr = "";
    $imageErr = "";

    $jovaltok = ['Manuális','Automata'];
    $jouzemanyag = ['Benzin','Dízel','Hybrid','Elektromos'];

    if($_POST){
        if(!empty($_POST['brand'])){
            $brand = $_POST['brand'];
        }else{
            $brandErr = "A márkát meg kell adni!";
            $mindenjo = false;
        }
        if(!empty($_POST['model'])){
            $model = $_POST['model'];
        }else{
            $modelErr = "A modellt meg kell adni";
            $mindenjo = false;
        }
        if(!empty($_POST['year']) && $_POST['year'] > 0 && preg_match('/^[1-9][0-9]{3}$/', $_POST['year'])){
            $year = intval($_POST['year']);
        }else{
            $yearErr = "Az évet add meg helyesen! (Helyes formátum pl: 1992)";
            $mindenjo = false;
        }
        if(!empty($_POST['gearbox']) && in_array($_POST['gearbox'],$jovaltok)){
            $gearbox = $_POST['gearbox'] == 'Manuális' ? 'Manual': 'Automatic';
        }else{
            $gearboxErr = "Csak listában szereplő váltót kötelező megadni!";
            $mindenjo = false;
        }
        if(!empty($_POST['fuel_type']) && in_array($_POST['fuel_type'],$jouzemanyag)){
            if($_POST['fuel_type'] == "Benzin"){
                $fuel_type = "Petrol";
            }else if($_POST['fuel_type'] == "Dízel"){
                $fuel_type = "Diesel";
            }else if($_POST['fuel_type'] == "Hybrid"){
                $fuel_type = "Hybrid";
            }else{
                $fuel_type = "Electric";
            }
        }else{
            $fuel_typeErr = "Köztelező megadni az üzemanyag típust a listából!";
            $mindenjo = false;
        }
        if(!empty($_POST['passengers']) && preg_match('/^[1-9][0-9]{0,}$/', $_POST['passengers'])){
            $passengers = intval($_POST['passengers']);
        }else{
            $passengersErr = "Az ülések számát muszály megadni! Pl: 1,2,12";
            $mindenjo = false;
        }
        if(!empty($_POST['daily_price_huf']) && preg_match('/^[1-9][0-9]{0,}$/', $_POST['daily_price_huf'])){
            $daily_price_huf = intval($_POST['daily_price_huf']);
        }else{
            $daily_price_hufErr = "A napi árat muszáj meadni! Pl: 16500";
            $mindenjo = false;
        }
        if(!empty($_POST['image'])){
            $image = $_POST['image'];
        }else{
            $imageErr = "A képet muszáj vagy megadni!";
            $mindenjo = false;
        }

        if($mindenjo){
            $elem=[
                "id" => end($cars)['id']+1,
                "brand" => $brand,
                "model"=> $model,
                "year" => $year,
                "transmission" => $gearbox,
                "fuel_type" => $fuel_type,
                "passengers" => $passengers,
                "daily_price_huf" => $daily_price_huf,
                "image" => $image
            ];

            array_push($cars,$elem);
            writeJSON("cars.json",$cars);
            $brand = "";
            $model = "";
            $year = "";
            $gearbox = "";
            $fuel_type = "";
            $passengers = "";
            $daily_price_huf = "";
            $image = "";
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
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <?php if($_POST && $mindenjo):?>
                        <p id="success">Sikeresen mentetted az autót</p>
                    <?php endif?>
                    <form action="" method="POST" id="addform" class="text-center">
                        <label for="brand">Márka</label><br>
                        <input type="text" id="brand" name="brand" placeholder="brand" value="<?= $brand?>"><br>
                        <span class="err"><?= $brandErr?></span><br>
                        <label for="model">Model</label><br>
                        <input type="text" id="model" name="model" placeholder="model" value="<?= $model?>"><br>
                        <span class="err"><?= $modelErr?></span><br>
                        <label for="year">Gyártási év</label><br>
                        <input type="text" id="year" name="year" placeholder="year" value="<?= $year?>"><br>
                        <span class="err"><?= $yearErr?></span><br>
                        <select id="gearbox" name="gearbox" class="form-select">
                            <option value="" <?= $gearbox == "" ? 'selected': ''?>>Váltó típusa</option>
                            <option value="Manuális" <?= $gearbox == "Manual" ? 'selected': ''?>>Manuális</option>
                            <option value="Automata" <?= $gearbox == "Automatic" ? 'selected': ''?>>Automata</option>
                        </select><br>
                        <span class="err"><?= $gearboxErr?></span><br>
                        <label for="passengers">Üzemanyag típusa</label><br>
                        <select id="fuel_type" name="fuel_type" class="form-select">
                            <option value="" <?= $fuel_type == "" ? 'selected': ''?>>Üzemanyag típusa</option>
                            <option value="Benzin" <?= $fuel_type == "Petrol" ? 'selected': ''?>>Benzin</option>
                            <option value="Dízel" <?= $fuel_type == "Diesel" ? 'selected': ''?>>Dízel</option>
                            <option value="Hybrid" <?= $fuel_type == "Hybrid" ? 'selected': ''?>>Hybrid</option>
                            <option value="Elektromos" <?= $fuel_type == "Electric" ? 'selected': ''?>>Elektromos</option>
                        </select><br>
                        <span class="err"><?= $fuel_typeErr?></span><br>
                        <label for="passengers">Ülések száma</label><br>
                        <input type="text" id="passengers" name="passengers" placeholder="passengers" value="<?= $passengers?>"><br>
                        <span class="err"><?= $passengersErr?></span><br>
                        <label for="daily_price_huf">Napi ár</label><br>
                        <input type="text" id="daily_price_huf" name="daily_price_huf" placeholder="daily_price_huf" value="<?= $daily_price_huf?>"><br>
                        <span class="err"><?= $daily_price_hufErr?></span><br>
                        <label for="image">Kép</label><br>
                        <input type="text" id="image" name="image" placeholder="image" value="<?= $image?>"><br>
                        <span class="err"><?= $imageErr?></span><br>
                        <button type="submit" class="btn btn-warning w-100">Felvétel</button>
                    </form>
                </div>
            </div>
        </div>
</body>
</html>