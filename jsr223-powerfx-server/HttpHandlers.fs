namespace jsr223_powerfx_server

module HttpHandlers =

    open Microsoft.AspNetCore.Http
    open FSharp.Control.Tasks
    open Giraffe
    open jsr223_powerfx_server.Models
    open jsr223_powerfx_server.PowerFxScopeFactory
    open Microsoft.PowerFx.Core.Public.Values;
    open jsr223_powerfx_server.PowerFxHelper

    let evalRequest(evalRequest: EvalRequest): EvalResponse =
        try
            let engine = PowerFxScopeFactory().GetEngine()
            let parameters: RecordValue = FormulaValue.FromJson(evalRequest.context) :?> RecordValue
            let result = engine.Eval(evalRequest.expression, parameters)
            System.Console.WriteLine result
            let response: EvalResponse = {
                result = valToString(result)
            }
            response
        with
        | _ -> { result = null }

    let handleEval =
        fun (next : HttpFunc) (ctx : HttpContext) ->
            task {
                let! request = ctx.BindJsonAsync<EvalRequest>()
                let response = evalRequest(request)
                return! json response next ctx
            }

    let handleGetHello =
        fun (next : HttpFunc) (ctx : HttpContext) ->
            task {
                let response = {
                    Text = "Hello world, from Giraffe!"
                }
                return! json response next ctx
            }