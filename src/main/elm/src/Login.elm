module Login exposing (Model, Msg, view, update,  initialModel)

import Auth
import Browser exposing (Document)
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import Http
import Json.Encode as Encode
import Json.Decode as Decode

import Utils
import Auth



type alias Model =
    { form : Form
    , problems : List Problem
    , authInfo : Auth.AuthInfo
    }

type ValidatedField
    = Email
    | Password

fieldsToValidate : List ValidatedField
fieldsToValidate =
    [ Email
    , Password
    ]

type Problem
    = InvalidEntry ValidatedField String
    | ServerError String
    | JsonError String

{-| Marks that we've trimmed the form's fields, so we don't accidentally send
it to the server without having trimmed it!
-}
type TrimmedForm
    = Trimmed Form

type alias Form =
    { email : String
    , password : String
    }



type Msg
    = SubmittedForm
    | EnteredEmail String
    | EnteredPassword String
    | CompletedLogin (Result Http.Error String)


-- init


initialModel : Model
initialModel =
    { form =
        { email = ""
        , password = ""
        }
    , problems = []
    , authInfo = Auth.emptyAuthInfo
    }

view : Model -> { title : String, content : Html Msg }
view model =
    { title = "Login"
    , content =  viewForm model.form
    }



viewForm: Form -> Html Msg
viewForm form =
    Html.form [ onSubmit SubmittedForm ]
        [
        label [for "email-text"] [
            div [] [text "login"]
            , input [ type_ "text", placeholder "Введите текст", id "input-text", value form.email, onInput EnteredEmail ] []
        ]
        ,label [for "password-text"] [
            div [] [text "password"]
            , input [ type_ "password", placeholder "Введите пароль", id "password-text", value form.password, onInput EnteredPassword ] []
        ]
        , button []       [ text "Sign in" ]
        ]

-- update
update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        SubmittedForm ->
            case validate model.form of
                Ok validForm ->
                    ({ model | problems = []}
                    , Http.post {url = "/login"
                                , body = Http.jsonBody (login validForm)
                                , expect = Http.expectJson CompletedLogin Decode.string
                    } )

                Err problems ->
                    ( { model | problems = problems } , Cmd.none)

        EnteredEmail email ->
            updateForm (\form -> { form | email = email }) model

        EnteredPassword password ->
            updateForm (\form -> { form | password = password }) model

        CompletedLogin (Err error) ->
            let
                serverErrors =
                    Utils.decodeErrors error
                        |> List.map ServerError
            in
            ( { model | problems = List.append model.problems serverErrors }
            , Cmd.none
            )

        CompletedLogin (Ok str) ->
            let
                authInfoR = Auth.decodeAuth str
            in
            case authInfoR of
                Ok authInfo ->
                    ({ model | authInfo = authInfo}, Auth.storeAuth authInfo)

                Err error->
                    let
                        jsonErrors =
                            Utils.decodeJsonErrors error
                                |> List.map JsonError
                    in
                    ( { model | problems = List.append model.problems jsonErrors }
                    , Cmd.none
                    )



{-| Helper function for `update`. Updates the form and returns Cmd.none.
Useful for recording form fields!
-}
updateForm : (Form -> Form) -> Model -> ( Model, Cmd Msg )
updateForm transform model =
    ( { model | form = transform model.form }, Cmd.none )



-- Utilites

{-| Trim the form and validate its fields. If there are problems, report them!
-}
validate : Form -> Result (List Problem) TrimmedForm
validate form =
    let
        trimmedForm =
            trimFields form
    in
    case List.concatMap (validateField trimmedForm) fieldsToValidate of
        [] ->
            Ok trimmedForm

        problems ->
            Err problems


{-| Don't trim while the user is typing! That would be super annoying.
Instead, trim only on submit.
-}
trimFields : Form -> TrimmedForm
trimFields form =
    Trimmed
        { email = String.trim form.email
        , password = String.trim form.password
        }

validateField : TrimmedForm -> ValidatedField -> List Problem
validateField (Trimmed form) field =
    List.map (InvalidEntry field) <|
        case field of
            Email ->
                if String.isEmpty form.email then
                    [ "email can't be blank." ]

                else
                    []

            Password ->
                if String.isEmpty form.password then
                    [ "password can't be blank." ]

                else
                    []



login : TrimmedForm -> Encode.Value
login (Trimmed form) =
            Encode.object
                [ ( "username", Encode.string form.email )
                , ( "password", Encode.string form.password )
                ]


{-
decodeLoginResult : Decode.Decoder Auth.AuthInfo
decodeLoginResult =
    Decode.map5
        Auth.AuthInfo
        (Decode.field "username" Decode.string)
        (Decode.field "roles" (Decode.list Decode.string))
        (Decode.field "access_token" Decode.string)
        (Decode.field "token_type" Decode.string)
        (Decode.field "expires_in" Decode.int)
-}