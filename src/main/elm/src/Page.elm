module Page exposing (Page(..),  view, blankView, aboutView )

import Browser exposing (Document)
import Html exposing (Html, Attribute, a, button, div, footer, i, img, li, nav, p, span, text, ul, node)
import Html.Attributes exposing (class, classList, href, style)
import Html.Events exposing (onClick)



import Route exposing (Route)
import Config

type Page
    = About
    | Login
    | Data


{-| Take a page's Html and frames it with a header and footer.

The caller provides the current user, so we can display in either
"signed in" (rendering username) or "signed out" mode.

isLoading is for determining whether we should show a loading spinner
in the header. (This comes up during slow page transitions.)

-}
view : Maybe String -> Page -> { title : String, content : Html msg } -> Document msg
view maybeViewer page { title, content } =
    { title = title ++ " - " ++ Config.siteName
    , body = viewHeader page maybeViewer :: content :: [ viewFooter ]
    }

viewHeader : Page -> Maybe String -> Html msg
viewHeader page maybeViewer =
    nav [ class "navbar navbar-light" ]
        [ div [ class "container" ]
            [ a [ class "navbar-brand", Route.href Route.Home ]
                [ text "conduit" ]
            , ul [ class "nav navbar-nav pull-xs-right" ] <|
                navbarLink page Route.Home [ text "Home" ]
                    :: viewMenu page maybeViewer
            ]
        ]

viewMenu : Page -> Maybe String -> List (Html msg)
viewMenu page maybeViewer =
    let
        linkTo =
            navbarLink page
    in
    case maybeViewer of
        Just username ->
            [ linkTo Route.NewArticle [ i [ class "ion-compose" ] [], text "\u{00A0}New Post" ]
            , linkTo Route.Settings [ i [ class "ion-gear-a" ] [], text "\u{00A0}Settings" ]
--            , linkTo
--                (Route.Profile username)
--                [ img [ class "user-pic" ] []
--                , Html.text username
--                ]
            , linkTo Route.Logout [ text "Sign out" ]
            ]

        Nothing ->
            [ linkTo Route.Login [ text "Sign in" ]
            , linkTo Route.Register [ text "Sign up" ]
            ]

viewFooter : Html msg
viewFooter =
    footer []
        [ div [ class "container" ]
            [ a [ class "logo-font", href "/" ] [ text "conduit" ]
            , span [ class "attribution" ]
                [ text "An interactive learning project from "
                , a [ href "https://thinkster.io" ] [ text "Thinkster" ]
                , text ". Code & design licensed under MIT."
                ]
            ]
        ]

blankView : Maybe String -> Page -> { title : String, content : Html msg } -> Document msg
blankView maybeViewer page { title, content } =
    { title = title ++ " - " ++ Config.siteName
--    , body = blankViewHeader page maybeViewer :: content ::  [ blankViewFooter ]
    , body = List.singleton (mainTag [class "container"] [blankViewHeader page maybeViewer,  content, blankViewFooter ])
    }

blankViewHeader : Page -> Maybe String -> Html msg
blankViewHeader page maybeViewer =
    nav [] [text "header"]

blankViewFooter : Html msg
blankViewFooter =
    footer [] [text "footer"]



navbarLink : Page -> Route -> List (Html msg) -> Html msg
navbarLink page route linkContent =
    li [ classList [ ( "nav-item", True ), ( "active", isActive page route ) ] ]
        [ a [ class "nav-link", Route.href route ] linkContent ]


isActive : Page -> Route -> Bool
isActive page route =
    case ( page, route ) of
--        ( Home, Route.Home ) ->
--            True

        ( Login, Route.Login ) ->
            True

--        ( Register, Route.Register ) ->
--            True

--        ( Settings, Route.Settings ) ->
--            True

--        ( Profile pageUsername, Route.Profile routeUsername ) ->
--            pageUsername == routeUsername

--        ( NewArticle, Route.NewArticle ) ->
--            True

        _ ->
           False


{-| Render dismissable errors. We use this all over the place!
-}
viewErrors : msg -> List String -> Html msg
viewErrors dismissErrors errors =
    if List.isEmpty errors then
        Html.text ""

    else
        div
            [ class "error-messages"
            , style "position" "fixed"
            , style "top" "0"
            , style "background" "rgb(250, 250, 250)"
            , style "padding" "20px"
            , style "border" "1px solid"
            ]
        <|
            List.map (\error -> p [] [ text error ]) errors
                ++ [ button [ onClick dismissErrors ] [ text "Ok" ] ]

aboutView : { title : String, content : Html msg }
aboutView =
    {title = "qwe"
    , content = div [] [text "about"]
    }


mainTag : List (Attribute msg) -> List (Html msg) -> Html msg
mainTag attributes children =
 node "main" attributes children
