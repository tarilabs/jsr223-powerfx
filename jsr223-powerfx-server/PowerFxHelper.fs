module jsr223_powerfx_server.PowerFxHelper

open Microsoft.PowerFx.Core.Public.Values
open System
open System.Linq

let dRecToFormula (x: DValue<RecordValue>) = x.ToFormulaValue()
let dRecColumnToFormula (x: DValue<RecordValue>) = x.Value.Fields.First().Value

let rec valToString (v : FormulaValue): string =
    match v with
    | :? NumberValue as x -> $"{x.Value}"
    | :? BooleanValue as x -> $"{x.Value}"
    | :? StringValue as x -> "\"" + x.Value.Replace("\"", "\\\"") + "\""
    | :? TableValue as t ->
        if t.IsColumn then
            let x =
                t.Rows
                |> Seq.map dRecColumnToFormula
                |> Seq.map valToString
                |> Seq.reduce (fun a b -> a + "," + b)
            "[" + x + "]"
        else
            let x =
                t.Rows
                |> Seq.map dRecToFormula
                |> Seq.map valToString
                |> Seq.reduce (fun a b -> a + "," + b)
            "[" + x + "]"
    | :? RecordValue as r ->
        let fields = r.Fields.ToArray() // we want to react on size
        if fields.Length = 1 && fields[0].Name = "Value" then
            "[" + valToString(fields[0].Value) + "]"
        else
            let sorted =
                fields
                |> Array.sortWith (fun a b -> String.CompareOrdinal(a.Name, b.Name))
                |> Array.map (fun x -> x.Name + ":" + valToString(x.Value))
                |> Seq.reduce (fun a b -> a + "," + b)
            "{" + sorted + "}"
    | :? BlankValue -> "{}"
    | _ -> "\"<unknown>\""

