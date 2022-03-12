namespace jsr223_powerfx_server.PowerFxScopeFactory

open Microsoft.PowerFx
open Microsoft.PowerFx.Core
open Microsoft.PowerFx.Core.Public
open System.Globalization
open System.Web
open System

type PowerFxScopeFactory() =
    member this.GetEngine(): RecalcEngine =
        (
            Console.WriteLine CultureInfo.CurrentCulture
            CultureInfo.CurrentCulture <- new CultureInfo("en-US")
            Console.WriteLine CultureInfo.CurrentCulture
            let engine = new RecalcEngine()
            engine
        )
    member this.GetScope(contextJson: string): RecalcEngineScope = 
        (
            let engine = this.GetEngine()
            RecalcEngineScope.FromJson(engine, contextJson);
        )
    interface IPowerFxScopeFactory with
        member this.GetOrCreateInstance(documentUri: string): IPowerFxScope =
            (         
                let uriObj = new Uri(documentUri);
                let mutable contextJson = HttpUtility.ParseQueryString(uriObj.Query).Get("context");
                if (contextJson = null) then contextJson <- "{}"
                    
                let scope = this.GetScope(contextJson);
                scope
            )