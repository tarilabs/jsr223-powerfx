module jsr223_powerfx_server.PowerFxHelper

open Microsoft.PowerFx.Core.Public.Values
open System
open System.Linq

let dRecToFormula (x: DValue<RecordValue>) = x.ToFormulaValue()
let dRecColumnToFormula (x: DValue<RecordValue>) = x.Value.Fields.First().Value

let tableValueToString (t: TableValue) (recFn: FormulaValue -> string) = 
    if t.IsColumn then
        let x =
            t.Rows
            |> Seq.map dRecColumnToFormula
            |> Seq.map recFn
            |> Seq.reduce (fun a b -> a + "," + b)
        "[" + x + "]"
    else
        let x =
            t.Rows
            |> Seq.map dRecToFormula
            |> Seq.map recFn
            |> Seq.reduce (fun a b -> a + "," + b)
        "[" + x + "]"

let recordValue (r: RecordValue) (recFn: FormulaValue -> string) = 
    let fields = r.Fields.ToArray() // we want to react on size
    if fields.Length = 1 && fields[0].Name = "Value" then
        "[" + recFn(fields[0].Value) + "]"
    else
        let sorted =
            fields
            |> Array.sortWith (fun a b -> String.CompareOrdinal(a.Name, b.Name))
            |> Array.map (fun x -> x.Name + ":" + recFn(x.Value))
            |> Seq.reduce (fun a b -> a + "," + b)
        "{" + sorted + "}"

let rec valToString (v : FormulaValue): string =
    match v with
    | :? NumberValue as x -> $"{x.Value}"
    | :? BooleanValue as x -> $"{x.Value}"
    | :? StringValue as x -> "\"" + x.Value.Replace("\"", "\\\"") + "\""
    | :? TableValue as t -> tableValueToString t valToString
    | :? RecordValue as r -> recordValue r valToString
    | :? BlankValue -> "{}"
    | _ -> "\"<unknown>\""

