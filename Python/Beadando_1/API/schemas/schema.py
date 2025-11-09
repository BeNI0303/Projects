from pydantic import BaseModel, EmailStr, Field, field_validator

'''

Útmutató a fájl használatához:

Az osztályokat a schema alapján ki kell dolgozni.

A schema.py az adatok küldésére és fogadására készített osztályokat tartalmazza.
Az osztályokban az adatok legyenek validálva.
 - az int adatok nem lehetnek negatívak.
 - az email mező csak e-mail formátumot fogadhat el.
 - Hiba esetén ValuErrort kell dobni, lehetőség szerint ezt a 
   kliens oldalon is jelezni kell.

'''

ShopName='Bolt'

class User(BaseModel):
    id: int = Field(..., ge=0)
    name: str
    email: EmailStr

    @field_validator('id')
    @classmethod
    def validate_id(cls, i):
        if i < 0:
            raise ValueError("Az ID nem lehet negatív szám!")
        return i


class Basket(BaseModel):
    id: int = Field(..., ge=0)
    user_id: int = Field(..., ge=0)
    items: list

    @field_validator('id')
    @classmethod
    def validate_id(cls, i):
        if i < 0:
            raise ValueError("Az ID nem lehet negatív szám!")
        return i
    
    @field_validator('user_id')
    @classmethod
    def validate_userid(cls, i):
        if i < 0:
            raise ValueError("A user_id nem lehet negatív szám!")
        return i

class Item(BaseModel):
    item_id: int = Field(..., ge=0)
    name: str
    brand: str
    price: float = Field(..., ge=0)
    quantity: int = Field(..., ge=0)

    @field_validator('item_id')
    @classmethod
    def validate_id(cls, i):
        if i < 0:
            raise ValueError("Az item_id nem lehet negatív szám!")
        return i
    
    @field_validator('quantity')
    @classmethod
    def validate_userid(cls, i):
        if i < 0:
            raise ValueError("A quantity nem lehet negatív szám!")
        return i   

    @field_validator('price')
    @classmethod
    def validate_userid(cls, i):
        if i < 0:
            raise ValueError("A price nem lehet negatív szám!")
        return i   