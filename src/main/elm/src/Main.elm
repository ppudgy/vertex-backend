module Main exposing (..)

import Browser exposing (Document)
import Browser.Navigation as Nav
import Html exposing (Html, Attribute, div)
import Json.Encode exposing (Value)
import Url exposing (Url)
import Ports

-- local packages
import Page
import Auth exposing (AuthInfo, loadAuth)
import Login
import Pages


-- MODEL

type Model
    = Login Login.Model
    | Pages Pages.Model
    | About


initialModel : Maybe AuthInfo -> Model
initialModel maybeInfo =
    case maybeInfo of
        Just info ->
            Pages (Pages.initialModel info)

        Nothing ->
            Login Login.initialModel

init : Value -> Url.Url -> Nav.Key -> (Model, Cmd msg)
init value url key =
    (initialModel (Auth.loadAuth value), Cmd.none)



-- UPDATE

type Msg
    = ChangedUrl Url
    | ClickedLink Browser.UrlRequest
    | Logout
    | GotAboutMsg Pages.Msg
    | GotLoginMsg Login.Msg
    | GotPagesMsg Pages.Msg

update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
    case (msg, model) of
        (Logout, _) ->
            (Login (Login.initialModel) , Ports.storeAuth Nothing)

        (ChangedUrl url, _) ->
            (model, Cmd.none)

        (ClickedLink urlRequest, _) ->
            (model, Cmd.none)

        (GotLoginMsg subMsg, Login login) ->
            Login.update subMsg login
                |> updateWith Login GotLoginMsg model

        (GotPagesMsg subMsg, Pages subModel) ->
            Pages.update subMsg subModel
                |> updateWith Pages GotPagesMsg model

        ( _, _ ) ->
            -- Disregard messages that arrived for the wrong page.
            ( model, Cmd.none )

updateWith : (subModel -> Model) -> (subMsg -> Msg) -> Model -> ( subModel, Cmd subMsg ) -> ( Model, Cmd Msg )
updateWith toModel toMsg model ( subModel, subCmd ) =
    ( toModel subModel
    , Cmd.map toMsg subCmd
    )


-- SUBSCRIPTIONS


subscriptions : Model -> Sub Msg
subscriptions model =
  Sub.none

-- VIEW

view : Model -> Document Msg
view model =
    let
        viewer =
            Just "user"

        viewPage page toMsg config =
            let
                { title, body } =
                    Page.view viewer page config
            in
            { title = title
            , body = List.map (Html.map toMsg) body
            }

        viewBlankPage page toMsg config =
            let
                { title, body } =
                    Page.blankView viewer page config
            in
            { title = title
            , body = List.map (Html.map toMsg) body
            }

    in
    case model of
        Login login ->
            viewBlankPage Page.Login GotLoginMsg (Login.view login)

        About ->
            viewBlankPage Page.About GotAboutMsg Page.aboutView


        Pages subModel ->
            viewPage Page.Data  GotPagesMsg (Pages.view subModel)

-- MAIN

main =  Browser.application
    { init = init
        , onUrlChange = ChangedUrl
        , onUrlRequest = ClickedLink
        , subscriptions = subscriptions
        , update = update
        , view = view
    }

