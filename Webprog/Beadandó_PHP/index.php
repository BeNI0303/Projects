<?php 
    session_start();

    $bejelentkezve = isset($_SESSION['user']);
    $admin = false;

    if($bejelentkezve && $_SESSION['user']['fullname'] == "admin"){
        $admin = true; 
    }

    $szures = false;

    $cars = readJSON("cars.json");

    $foglalasok = readJSON("foglalas.json");

    $seat = 0;
    $dateFrom = "";
    $dateTo = "";
    $gearbox = "";
    $rangeFrom = "";
    $rangeTo = "";
    $filterCars = [];

    if($_POST){
        if(isset($_POST['seats'])){
            $seat = $_POST['seats'];
        }
        if(isset($_POST['dateFrom'])){
            $dateFrom = $_POST['dateFrom'];
        }
        if(isset($_POST['dateTo'])){
            $dateTo = $_POST['dateTo'];
        }
        if(isset($_POST['gearbox'])){
            $gearbox = $_POST['gearbox'];
        }
        if(isset($_POST['rangeFrom'])){
            $rangeFrom = $_POST['rangeFrom'];
        }
        if(isset($_POST['rangeTo'])){
            $rangeTo = $_POST['rangeTo'];
        }
        
        foreach ($cars as $car) {
            $match = true;
            if ($seat > 0 && $car['passengers'] != $seat) {
                $match = false;
            }

            foreach ($foglalasok as $foglalas) {
                if($foglalas['carId'] == $car['id']){
                    if (!empty($dateFrom) && $foglalas['fromDate'] >= $dateFrom && $foglalas['toDate'] <= $dateTo) {
                        $match = false;
                    }else if(!empty($dateFrom) && $foglalas['fromDate'] >= $dateFrom && $foglalas['toDate'] > $dateTo && $foglalas['fromDate'] < $dateTo){
                        $match = false;
                    }else if(!empty($dateFrom) && $foglalas['fromDate'] < $dateFrom && $foglalas['toDate'] <= $dateTo && $foglalas['toDate'] > $dateFrom){
                        $match = false;
                    }
                }
            }
    
            if (!empty($gearbox) && $car['transmission'] != $gearbox) {
                $match = false;
            }
            if (!empty($rangeFrom) && !empty($rangeTo) && ($car['daily_price_huf'] < $rangeFrom || $car['daily_price_huf'] > $rangeTo)) {
                $match = false;
            }
    
            if ($match) {
                array_push($filterCars,$car);
            }
        }

    }else{
        $filterCars = $cars;
    }

    if($_GET && isset($_GET['id'])){
        $deletedCars = [];
        foreach ($filterCars as $car) {
            if($car['id'] != $_GET['id']){
                array_push($deletedCars,$car);
            }
        }
        $filterCars = $deletedCars;
        writeJSON("cars.json",$deletedCars);
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
                <div class="col-6">
                    <h1>Kölcsönözz autókat könnyedén!</h1>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-2">
                    <a href="register.php" class="reg">Regisztráció</a>
                </div>
            </div>
            <div class="row">
                <div class="col-6">
                </div>
                <div class="col-6">
                    <form action="" method="POST">
                        <div class="row">
                            <div class="col-md-10">
                                <div class="row">
                                    <div class="col-md-1 buttons">
                                        <input type="button" id="pluss" value="+" class="control">
                                    </div>
                                    <div class="col-md-1 seatNum">
                                        <input type="text" id="seats" name="seats" class="form-control" placeholder="0" min="0" value="<?= $seat?>" disabled>
                                    </div>
                                    <div class="col-md-1 buttons">
                                        <input type="button" id="minus" value="-" class="control">
                                    </div>
                                    <div class="col-md-2 text-center buttons">
                                        <p>Férőhely</p>
                                    </div>
                                    <div class="col-md-3 datein">
                                        <div class="row">
                                            <div class="col-md-10 date">
                                                <input type="text" id="date-start" name="dateFrom" class="form-control" placeholder="2024.10.04" value="<?= $dateFrom?>">
                                            </div>
                                            <div class="col-md-2 date">
                                                <p>-tól</p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-3 datein">
                                        <div class="row">
                                            <div class="col-md-10 date">
                                                <input type="text" id="date-end" name="dateTo" class="form-control" placeholder="2024.10.04" value="<?= $dateTo?>">
                                            </div>
                                            <div class="col-md-2 date">
                                                <p>-ig</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row sor2">
                                    <div class="col-md-4"></div>
                                    <div class="col-md-3">
                                        <select id="gearbox" name="gearbox" class="form-select" >
                                            <option value="" <?= $gearbox == "" ? "selected" : ""?>>Váltó típusa</option>
                                            <option value="manual" <?= $gearbox == "manual" ? "selected" : ""?>>Manuális</option>
                                            <option value="automatic" <?= $gearbox == "automatic" ? "selected" : ""?>>Automata</option>
                                        </select>
                                    </div>
                                    <div class="col-md-5">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <input type="text" name="rangeFrom" class="form-range price" min="14000" max="21000" step="1000" placeholder="14.000" value="<?= $rangeFrom?>">
                                            </div>
                                            <div class="col-md-1"> - </div>
                                            <div class="col-md-4">
                                                <input type="text" name="rangeTo" class="form-range price" min="14000" max="21000" step="1000" placeholder="21.000" value="<?= $rangeTo?>">
                                            </div>
                                            <div class="col-md-2"> Ft</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <button type="submit" class="btn btn-warning w-100">Szűrés</button>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
            <br>
            <div class="row">
                <?php foreach ($filterCars as $car) { ?>
                    <div class="col-md-2">
                        <div class="card bg-secondary text-light">
                            <?php if($admin):?>
                                <a href="index.php?id=<?= $car['id']?>" class="delete">Törlés</a>
                                <a href="edit.php?id=<?= $car['id']?>" class="edit">Szerkeszt</a>
                            <?php endif?>
                            <div>
                                <?php if(!$admin):?>
                                    <a href="car.php?id=<?=$car['id']?>" class="stretched-link"></a>
                                <?php endif?>
                                <img src="<?= $car['image']?>" class="card-img-top carPics" alt="<?=$car['brand']." ".$car['model']?>">
                                <p class="cprice"><?= number_format($car['daily_price_huf'],0,",",".")?> Ft</p>
                                <div class="card-body text-center">
                                    <div class="row">
                                        <div class="col-md-7">
                                            <p class="detail"><?= $car['brand']?> <b><?= $car['model']?> </b></p>
                                            <p class="detail"><?= $car['passengers']?> férőhely - <?= $car['transmission']?></p>
                                        </div>
                                        <div class="col-md-5">
                                            <a href="#" class="btn btn-warning">Foglalás</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                <?php } ?>
                <?php if($admin):?>
                    <div class="col-md-2">
                        <div class="card plus text-light">
                            <a href="add.php" class="stretched-link"></a>
                            <img src="plus.png" class="plus" alt="Nissan Altima">
                        </div>
                    </div>
                <?php endif?> 
            </div>
        </div>
        <script src="script.js"></script>
    </body>
</html>