module Index exposing (Model, Msg(..), initialModel, view, update)

import Browser exposing (Document)
import Html exposing (Html, div)


import Auth exposing (Token)

type alias Model =
    { session: Token
    }


initialModel : Token -> Model
initialModel token =
    { session = token}


type Msg = None





view : Model -> { title : String, content : Html Msg }
view model =
    { title = "Index"
    , content =  div [] []
    }

-- update
update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    (model, Cmd.none)
