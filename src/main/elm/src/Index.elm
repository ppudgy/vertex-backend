module Index exposing (Model, initialModel)

import Auth exposing (Token)




type alias Model =
    { session: Token
    }





initialModel : Token -> Model
initialModel token =
    { session = token}