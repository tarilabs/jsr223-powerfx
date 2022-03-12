module jsr223_powerfx_server.PowerFxHelper

open Microsoft.PowerFx.Core.Public.Values;

let valToString (v : FormulaValue): string =
    match v with
    | :? NumberValue as x -> $"{x.Value}"
    | :? BooleanValue as x -> $"{x.Value}"
    | :? StringValue as x -> $"\"{x.Value}\""
    | _ -> "TODO"
