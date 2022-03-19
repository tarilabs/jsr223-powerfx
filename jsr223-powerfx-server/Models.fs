namespace jsr223_powerfx_server.Models

[<CLIMutable>]
type Message =
    {
        Text : string
    }

[<CLIMutable>]
type EvalRequest =
    {
        context : string
        expression : string
    }

[<CLIMutable>]
type EvalResponse = 
    {
        result : string
        error: string
    }