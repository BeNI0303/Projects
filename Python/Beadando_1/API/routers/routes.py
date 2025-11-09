from schemas.schema import User, Basket, Item
from fastapi.responses import JSONResponse, RedirectResponse
from fastapi import FastAPI, HTTPException, Request, Response, Cookie
from fastapi import APIRouter
from data.filehandler import (
    load_json,
    save_json,
    add_user,
    add_basket,
    add_item_to_basket,
)
from data.filereader import (
    get_user_by_id,
    get_basket_by_user_id,
    get_all_users,
    get_total_price_of_basket
)

'''

Útmutató a fájl használatához:

- Minden route esetén adjuk meg a response_modell értékét (típus)
- Ügyeljünk a típusok megadására
- A függvények visszatérési értéke JSONResponse() legyen
- Minden függvény tartalmazzon hibakezelést, hiba esetén dobjon egy HTTPException-t
- Az adatokat a data.json fájlba kell menteni.
- A HTTP válaszok minden esetben tartalmazzák a 
  megfelelő Státus Code-ot, pl 404 - Not found, vagy 200 - OK

'''

routers = APIRouter()

@routers.post('/adduser', response_model=User)
def adduser(user: User) -> User:
    try:
        exists = get_user_by_id(user.id)
        if exists:
            raise ValueError("Létezik már ilyen felhasználó!")
        
        add_user(user.dict())
        return JSONResponse(status_code=200, content=user.dict())
    except ValueError as e:
        raise HTTPException(status_code=422, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@routers.post('/addshoppingbag', response_model=str)
def addshoppingbag(userid: int) -> str:
    try:
        exists = get_user_by_id(userid)
        if not exists:
            raise ValueError("Nincs ilyen felhasználó!")
        
        new_basket = {
            "id": 0,
            "user_id": userid,
            "items": []
        }
        
        add_basket(new_basket)
        return JSONResponse(status_code=200, content={"message": "Sikeres kosár hozzárendelés"})
    except ValueError as e:
        raise HTTPException(status_code=422, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@routers.post('/additem', response_model=Basket)
def additem(userid: int, item: Item) -> Basket:
    try:
        exist = get_user_by_id(userid)
        if not exist:
            raise ValueError("Nincs ilyen felhasználó!")
        basket = get_basket_by_user_id(userid)[0]
        if not basket:
            raise ValueError("Nincs kosár a felhasználóhoz!")
        add_item_to_basket(userid, item.dict())
        return JSONResponse(status_code=200, content=get_basket_by_user_id(userid)[0])
    except ValueError as e:
        raise HTTPException(status_code=422, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@routers.put('/updateitem', response_model=Basket)
def updateitem(userid: int, itemid: int, updateItem: Item) -> Basket:
    try:
        data = load_json()
        basketCheck = next((b for b in data["Baskets"] if b["user_id"] == userid), None)
        if not basketCheck:
            raise ValueError("Kosár nem található.")

        found = False
        for basket in data["Baskets"]:
            if(basket["user_id"] == userid):
                for idx, item in enumerate(basket["items"]):
                    if item["item_id"] == itemid:
                        basketCheck = basket
                        basket["items"][idx] = updateItem.dict()
                        found = True

        if not found:
            raise ValueError("Termék nem található.")

        save_json(data)
        return JSONResponse(status_code=200, content=basketCheck)
    
    except ValueError as e:
        raise HTTPException(status_code=422, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@routers.delete('/deleteitem', response_model=Basket)
def deleteitem(userid: int, itemid: int) -> Basket:
    try:
        data = load_json()

        basket = None
        for b in data["Baskets"]:
            if(b['user_id'] == userid):
                basket = b
                basket['items'] = [i for i in basket['items'] if i['item_id'] != itemid]

        if not basket:
            raise ValueError("Kosár nem található")

        basket['items'] = [i for i in basket['items'] if i['item_id'] != itemid]
        save_json(data)
        return JSONResponse(status_code=200, content=basket)
    
    except ValueError as e:
        raise HTTPException(status_code=422, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@routers.get('/user', response_model=User)
def user(userid: int) -> User:
    try:
        user = get_user_by_id(userid)
        if not user:
            raise ValueError("Felhasználó nem található.")
        return JSONResponse(status_code=200, content=user)
    except ValueError as e:
        raise HTTPException(status_code=422, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@routers.get('/users', response_model=list[User])
def users() -> list[User]:
    try:
        users_list = get_all_users()
        if not users_list:
            raise ValueError("Nincsenek felhasználók.")
        return JSONResponse(status_code=200, content=users_list)
    except ValueError as e:
        raise HTTPException(status_code=422, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=422, detail=str(e))


@routers.get('/shoppingbag', response_model=list[Item])
def shoppingbag(userid: int) -> list[Item]:
    try:
        exists = get_user_by_id(userid)
        if not exists:
            raise ValueError(detail="Nincs ilyen felhasználó!",status_code=422)
        basket = get_basket_by_user_id(userid)[0]
        if not basket:
            raise ValueError("Kosár nem található.")
        return JSONResponse(status_code=200, content=basket['items'])
    except ValueError as e:
        raise HTTPException(status_code=422, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@routers.get('/getusertotal', response_model=float)
def getusertotal(userid: int) -> float:
    try:
        exists = get_user_by_id(userid)
        if not exists:
            raise ValueError(detail="Nincs ilyen felhasználó!")
        total = get_total_price_of_basket(userid)
        if total is None:
            raise ValueError("Kosár nem található.")
        return JSONResponse(status_code=200, content={"total": total})
    except ValueError as e:
        raise HTTPException(status_code=422, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))



