<?php 
    session_start();

    $bejelentkezve = isset($_SESSION['user']);
    $loggedin = "";
    $admin = false;
    $users = readJSON("regdata.json");

    $foglalasok = readJSON("foglalas.json");


    foreach ($users as $user) {
        if($_SESSION['user']['fullname'] == $user['fullname']){
            $loggedin = $user;
        }
    }

    if($bejelentkezve && ($_SESSION['user']['fullname'] == "admin")){
        $admin = true; 
    }

    if($_GET && isset($_GET['id'])){
        $deletedReserv = [];
        foreach ($foglalasok as $reservation) {
            if($reservation['id'] != $_GET['id']){
                array_push($deletedReserv,$reservation);
            }
        }
        $foglalasok = $deletedReserv;
        writeJSON("foglalas.json",$deletedReserv);
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
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="stylesheet" href="profile.css">
        <title>iKarRental</title>
        <style>
            
        </style>
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
                    <a href="profile.php" id="profile"><img id="profilep" src="<?=$_SESSION['user']['img']?>" alt=""></a>
                </nav>
            <?php endif?>
        </nav>
        <div class="container">
            <div class="profile">
                <img src="<?=$_SESSION['user']['img']?>" id="picture" alt="Profile Picture">
                <div class="profile-info">
                    <p>Bejelentkezve, mint</p>
                    <h1><?= $_SESSION['user']['fullname']?></h1>
                    <a href="login.php?logout=true" class="logout">Kijelentkezés</a>
                </div>
            </div>
            <div class="reservations">
                <?php if(!$admin){?>
                    <h2>Foglalásaim</h2>
                <?php }?>
                <?php if($admin){?>
                    <h2>Foglalások</h2>
                <?php }?>
                <div class="row">
                <?php if(!$admin){foreach ($loggedin['reservations'] as $reservation) { ?>
                    <div class="col-md-3">
                        <div class="card bg-secondary text-light">
                            <div>
                                <img src="<?= $reservation['image']?>" class="card-img-top carPics" alt="<?=$reservation['brand']." ".$reservation['model']?>">
                                <p class="cprice"><?= explode(".",$reservation['fromDate'])[1].".".explode(".",$reservation['fromDate'])[2]?> - <?= explode(".",$reservation['toDate'])[1].".".explode(".",$reservation['toDate'])[2]?></p>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <p class="detail"><?= $reservation['brand']?> <b><?= $reservation['model']?> </b></p>
                                            <p class="detail"><?= $reservation['passengers']?> férőhely - <?= $reservation['transmission']?></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                <?php }}if($admin) {foreach ($foglalasok as $reservation) { ?>
                    <div class="col-md-3">
                        <div class="card bg-secondary text-light">
                            <a href="profile.php?id=<?= $reservation['id']?>" class="delete">Törlés</a>
                            <div>
                                <img src="<?= $reservation['image']?>" class="card-img-top carPics" alt="<?=$reservation['brand']." ".$reservation['model']?>">
                                <p class="cprice"><?= explode(".",$reservation['fromDate'])[1].".".explode(".",$reservation['fromDate'])[2]?> - <?= explode(".",$reservation['toDate'])[1].".".explode(".",$reservation['toDate'])[2]?></p>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <p class="detail"><?= $reservation['brand']?> <b><?= $reservation['model']?> </b></p>
                                            <p class="detail"><?= $reservation['passengers']?> férőhely - <?= $reservation['transmission']?></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                <?php }} ?>
            </div>
        </div>
    </body>
</html>
