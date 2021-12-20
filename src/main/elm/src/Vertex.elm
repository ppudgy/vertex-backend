module Vertex exposing (..)

import Browser exposing (Document)
import Browser.Navigation as Nav
import Html exposing (Html, div)
import Json.Encode exposing (Value)
import Url exposing (Url)
import Jwt exposing (..)
import Ports

-- local package
import Auth exposing (Token)
import Index


-- MODEL

type Model
    = Login
    | Index Index.Model


initialModel : Maybe Token -> Model
initialModel maybeToken =
    case maybeToken of
        Just token ->
            Index (Index.initialModel token)

        Nothing ->
            Login

init : Value -> Url.Url -> Nav.Key -> (Model, Cmd msg)
init value url key =
    (initialModel (Auth.decodeTokenFromStore value), Cmd.none)



-- UPDATE



type Msg
    = ChangedUrl Url
    | ClickedLink Browser.UrlRequest
    | Logout

update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
    case msg of
        Logout ->
            (Login, Cmd.none)

        ChangedUrl url ->
            (model, Cmd.none)

        ClickedLink urlRequest ->
            (model, Cmd.none)




-- SUBSCRIPTIONS


subscriptions : Model -> Sub Msg
subscriptions model =
  Sub.none

-- VIEW

view : Model -> Document Msg
view model = { title = "---"
      , body = List.singleton (div [] [])
      }


-- MAIN


main =  Browser.application
    { init = init
        , onUrlChange = ChangedUrl
        , onUrlRequest = ClickedLink
        , subscriptions = subscriptions
        , update = update
        , view = view
    }
