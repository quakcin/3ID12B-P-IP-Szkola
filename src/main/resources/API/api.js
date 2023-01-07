/*
    Główny wrapper API, endpoint na stronach
 */

class HefajstosAPI
{
    constructor()
    {
        this.token = localStorage.getItem("hefajstos.token");
        if (this.token == undefined)
        {
            console.log("[HEFAJSTOS API] Token logowania nie występuje w pamięci lokalnej.");
            this.token = "brak";
        }
    }

    /*
        (new HefajstosAPI)
            .get((g) => console.log('items', g))
            .fail((e) => console.log('error', e);
            .req("zaloguj", ['usr', 'pwd']);
        ^                  ^                                   ^               ^
        ~~~~ endpoind      ~~~~ wysłanie żądania               ~~~~ obsłóga    ~~~~ błędy
     */

    wykonaj  (zadanie)
    {
        this.zadanie = zadanie;
        return this;
    }

    ewentualnie (blad)
    {
        this.blad = blad;
        return this;
    }

    dla (cmd, args)
    {
        /*
        server side rest api:
        cmd/TOKEN/arg0/arg1/arg2
         */
        const rest_request = ["", cmd, this.token, args].flat().join("/");
        console.log(rest_request);

        fetch(new Request(rest_request))
            .then(response => response.json())
            .then(json =>
            {
                console.log(this);
                if (json.ok == false)
                        this.blad(json);
                else
                    this.zadanie(json);
            });

    }

    mozeSieZepsuc()
    {
        this.blad = () => alert("Serwer teraz nie odpowiada, Skontaktuj się z Administratorem!");
        return this;
    }
}