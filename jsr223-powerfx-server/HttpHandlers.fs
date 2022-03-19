namespace jsr223_powerfx_server

module HttpHandlers =

    open Microsoft.AspNetCore.Http
    open FSharp.Control.Tasks
    open Giraffe
    open jsr223_powerfx_server.Models
    open jsr223_powerfx_server.PowerFxScopeFactory
    open jsr223_powerfx_server.PowerFxHelper
    open Microsoft.PowerFx.Core.Public.Values
    open Microsoft.PowerFx.LanguageServerProtocol
    open System.Collections.Generic

    let evalRequest(evalRequest: EvalRequest): EvalResponse =
        try
            let engine = PowerFxScopeFactory().GetEngine()
            let parameters: RecordValue = FormulaValue.FromJson(evalRequest.context) :?> RecordValue
            let result = engine.Eval(evalRequest.expression, parameters)
            System.Console.WriteLine result
            let response: EvalResponse = {
                result = valToString(result)
                error = null
            }
            response
        with
        | _ as e -> { result = null; error = e.ToString() }

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
    
    let handleLsp =
        fun (next : HttpFunc) (ctx : HttpContext) ->
            task {
                let! bodyString = ctx.ReadBodyFromRequestAsync()
                // System.Console.WriteLine("bodyString " + bodyString)
                let scopeFactory = new PowerFxScopeFactory()
                let sendToClientData = new List<string>()
                let addFn = fun (data: string) -> sendToClientData.Add(data)
                let languageServer = new LanguageServer(addFn, scopeFactory)
                languageServer.OnDataReceived(bodyString)
                let response = sendToClientData.ToArray()
                // System.Console.WriteLine response
                return! json response next ctx
            }
