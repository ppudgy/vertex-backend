module Auth exposing  (
    Token(..)
    , User(..)
    , Role(..)
    , AuthInfo
    , emptyAuthInfo
    , userToString
    , roleToString
    , tokenToString
    , decodeAuth
    , encodeAuth
    , storeAuth
    , loadAuth
    )

import Json.Decode as Decode exposing (Decoder)
import Json.Encode as Encode exposing (Value)

import Ports

type User
    = User String
    | Anonimous

type Role
    = Role String

type Token
    = Token String


type alias AuthInfo
    = { username: User
    , roles: List Role
    , access_token: Token
    , refresh_token: Token
    , token_type: String
    , expires_in: Int
    }


emptyAuthInfo: AuthInfo
emptyAuthInfo =
    { username = Anonimous
    , roles = []
    ,access_token = Token ""
    ,refresh_token = Token  ""
    , token_type = ""
    ,expires_in = 0
    }


userToString : User -> String
userToString user =
    case user of
        User name -> name
        Anonimous -> "anonimous"

roleToString : Role -> String
roleToString (Role name) =
    name

tokenToString : Token -> String
tokenToString (Token token) =
    token

-- STORAGE

decodeAuth : String -> Result Decode.Error AuthInfo
decodeAuth str =
        Decode.decodeString (
            Decode.map6 AuthInfo
                 (Decode.field "username" (Decode.map User Decode.string))
                 (Decode.field "roles" (Decode.list (Decode.map Role Decode.string)))
                 (Decode.field "access_token" (Decode.map Token Decode.string))
                 (Decode.field "refresh_token" (Decode.map Token Decode.string))
                 (Decode.field "token_type" Decode.string)
                 (Decode.field "expires_in" Decode.int)
            ) str

encodeAuth : AuthInfo -> Encode.Value
encodeAuth auth =
    Encode.object
    [("username", Encode.string (userToString auth.username))
    ,("roles", Encode.list Encode.string (List.map roleToString auth.roles))
    ,("access_token", Encode.string (tokenToString auth.access_token))
    ,("refresh_token", Encode.string (tokenToString auth.refresh_token))
    ,("token_type", Encode.string auth.token_type)
    ,("expires_in", Encode.int auth.expires_in)
    ]

storeAuth : AuthInfo -> Cmd msg
storeAuth auth =
    encodeAuth auth
        |> Encode.encode 0
        |> Just
        |> Ports.storeAuth

loadAuth : Value -> Maybe AuthInfo
loadAuth json =
    json
        |> Decode.decodeValue Decode.string
        |> Result.toMaybe
        |> Maybe.andThen (decodeAuth >> Result.toMaybe)

