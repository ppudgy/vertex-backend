module Pages exposing (
    Model(..)
    , Msg(..)
    , initialModel
    , update
    , view
    )

import Auth exposing (AuthInfo)
import Html exposing (Html, div, text)




type Model =
    Todo
    | Note

type Msg =
    None

initialModel : AuthInfo ->  Model
initialModel auth = Todo



update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
    (model, Cmd.none)



view : Model -> { title : String, content : Html Msg }
view model =
    { title = "Login"
    , content =  div [] [text "pages"]
    }