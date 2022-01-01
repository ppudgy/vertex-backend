module Utils exposing (..)


import Http
import Json.Decode as Decode exposing (Decoder, keyValuePairs, decodeString, field, list)
import String exposing (fromInt)

{-| Many API endpoints include an "errors" field in their BadStatus responses.
-}
decodeErrors : Http.Error -> List String
decodeErrors error =
    case error of
        Http.BadStatus num ->
            ["Server error" ++ fromInt num]
        _ ->
            [ "Server error" ]

{-| Many API endpoints include an "errors" field in their BadStatus responses.
-}
decodeJsonErrors : Decode.Error -> List String
decodeJsonErrors error =
    case error of
        Decode.Field name err ->
            ["JSON field error: " ++ name]

        Decode.Index index err ->
            ["JSON index error: " ++ fromInt index]

        Decode.OneOf list ->
            [ "JSON  error" ]

        Decode.Failure str val ->
            [ "JSON  failure: " ++ str]
