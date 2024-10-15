using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Runtime.InteropServices;
using System.Runtime.InteropServices.ComTypes;
using System.Threading;
using System.Threading.Tasks;

using Newtonsoft.Json;

namespace RestCuvinte
{
    public class Configurare
    {
        [JsonProperty("id")] public int Id { get; set; }
        [JsonProperty("cuvant")] public string Cuvant { get; set; }

        [JsonProperty("cuvantAmestecat")] public string CuvantAmestecat { get; set; }

        public Configurare()
        {
        }

        public Configurare(int id, string cuvant, string cuvantAmestecat)
        {
            Id = id;
            Cuvant = cuvant;
            CuvantAmestecat = cuvantAmestecat;
        }

        public override string ToString()
        {
            return $"[Configurare: id={Id}, cuvant={Cuvant}, litere={CuvantAmestecat}]";
        }

        class Program
        {
            static HttpClient client = new HttpClient(new LoggingHandler(new HttpClientHandler()));

            private static string URL_Base = "http://localhost:8080/jucatoriGames";

            public static void Main(string[] args)
            {
                RunAsync().Wait();
            }

            static async Task RunAsync()
            {
                client.BaseAddress = new Uri(URL_Base);
                client.DefaultRequestHeaders.Accept.Clear();
                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));


                Configurare configurare = new Configurare(-1, "Testttttt", "Testttttttt");
                bool saved = await SaveConfigurareAsync(URL_Base , configurare);
                if (saved)
                {
                    Console.WriteLine("Configurare saved successfully.");
                }
                else
                {
                    Console.WriteLine("Failed to save Configurare.");
                }
            }
            

            static async Task<bool> SaveConfigurareAsync(string path, Configurare configurare)
            {
                HttpResponseMessage response = await client.PostAsJsonAsync(path, configurare);
                return response.IsSuccessStatusCode;
            }
        }

        public class LoggingHandler : DelegatingHandler
        {
            public LoggingHandler(HttpMessageHandler innerHandler)
                : base(innerHandler)
            {
            }

            protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request,
                CancellationToken cancellationToken)
            {
                Console.WriteLine("Request:");
                Console.WriteLine(request.ToString());
                if (request.Content != null)
                {
                    Console.WriteLine(await request.Content.ReadAsStringAsync());
                }

                Console.WriteLine();

                HttpResponseMessage response = await base.SendAsync(request, cancellationToken);

                Console.WriteLine("Response:");
                Console.WriteLine(response.ToString());
                if (response.Content != null)
                {
                    Console.WriteLine(await response.Content.ReadAsStringAsync());
                }

                Console.WriteLine();

                return response;
            }
        }
    }
}



/*
namespace RestXO
{
    public class Configurare
    {
        [JsonProperty("id")]
        public int Id { get; set; }
        [JsonProperty("cerinta")]
        public string Cerinta { get; set; }
        
        public Configurare() {}
        
        public Configurare(int id, string cerinta)
        {
            Id = id;
            Cerinta = cerinta;
        }

        public override string ToString()
        {
            return $"[Configurare: id={Id}, cerinta={Cerinta}]";
        }
    }
    
     class Program
    {
        static HttpClient client = new HttpClient(new LoggingHandler(new HttpClientHandler()));

        private static string URL_Base = "http://localhost:8080/jucatoriGames";

        public static void Main(string[] args)
        {
            RunAsync().Wait();
        }

        static async Task RunAsync()
        {
            client.BaseAddress = new Uri(URL_Base);
            client.DefaultRequestHeaders.Accept.Clear();
            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
            
            int id = 1;
            string game = await GetJucatorAsync(URL_Base + "/jucator/" + id);
            if (game != null)
            { 
                Console.WriteLine("Am primit {0}", game);
            }
            else
            { 
                Console.WriteLine("No Game found with id: {0}", id);
            }
            await Task.Delay(1000);
            
            var updates = new Dictionary<string, string> { { "coef22", "DANA" } };
            bool updated = await UpdateConfigurareAsync(URL_Base + "/configurare/" + id, updates);
            if (updated)
            {
                Console.WriteLine("Game updated successfully.");
            }
            else
            {
                Console.WriteLine("Failed to update Game.");
            }
        }

        static async Task<string> GetJucatorAsync(string path)
        {
            string result = null;
            HttpResponseMessage response = await client.GetAsync(path);
            if (response.IsSuccessStatusCode)
            {
                result = await response.Content.ReadAsStringAsync();
            }
            return result;
        }

        static async Task<bool> UpdateConfigurareAsync(string path, Dictionary<string, string> updates)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync(path, updates);
            return response.IsSuccessStatusCode;
        }
    }

    public class LoggingHandler : DelegatingHandler
    {
        public LoggingHandler(HttpMessageHandler innerHandler)
            : base(innerHandler)
        {
        }
    
        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            Console.WriteLine("Request:");
            Console.WriteLine(request.ToString());
            if (request.Content != null)
            {
                Console.WriteLine(await request.Content.ReadAsStringAsync());
            }
            Console.WriteLine();
    
            HttpResponseMessage response = await base.SendAsync(request, cancellationToken);
    
            Console.WriteLine("Response:");
            Console.WriteLine(response.ToString());
            if (response.Content != null)
            {
                Console.WriteLine(await response.Content.ReadAsStringAsync());
            }
            Console.WriteLine();
    
            return response;
        }
    }
}
*/
/*
namespace RestVocale
{
    public class Configurare
    {
        [JsonProperty("id")] public int Id { get; set; }
        [JsonProperty("masca")] public string Masca { get; set; }

        [JsonProperty("cuvant")] public string Cuvant { get; set; }

        public Configurare()
        {
        }

        public Configurare(int id, string masca, string cuvant)
        {
            Id = id;
            Masca = masca;
            Cuvant = cuvant;
        }

        public override string ToString()
        {
            return $"[Configurare: id={Id}, masca={Masca}, cuvant={Cuvant}]";
        }

        class Program
        {
            static HttpClient client = new HttpClient(new LoggingHandler(new HttpClientHandler()));

            private static string URL_Base = "http://localhost:8080/jucatoriGames";

            public static void Main(string[] args)
            {
                RunAsync().Wait();
            }

            static async Task RunAsync()
            {
                client.BaseAddress = new Uri(URL_Base);
                client.DefaultRequestHeaders.Accept.Clear();
                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

                int id = 2;
                string game = await GetJucatorAsync(URL_Base + "/jucator/" + id);
                if (game != null)
                {
                    Console.WriteLine("Am primit {0}", game);
                }
                else
                {
                    Console.WriteLine("No Game found with id: {0}", id);
                }

                await Task.Delay(1000);
                Configurare configurare = new Configurare(-1, "xxxx", "cuvant");
                bool saved = await SaveConfigurareAsync(URL_Base , configurare);
                if (saved)
                {
                    Console.WriteLine("Configurare saved successfully.");
                }
                else
                {
                    Console.WriteLine("Failed to save Configurare.");
                }
            }

            static async Task<string> GetJucatorAsync(string path)
            {
                string result = null;
                HttpResponseMessage response = await client.GetAsync(path);
                if (response.IsSuccessStatusCode)
                {
                    result = await response.Content.ReadAsStringAsync();
                }

                return result;
            }

            static async Task<bool> SaveConfigurareAsync(string path, Configurare configurare)
            {
                HttpResponseMessage response = await client.PostAsJsonAsync(path, configurare);
                return response.IsSuccessStatusCode;
            }
        }

        public class LoggingHandler : DelegatingHandler
        {
            public LoggingHandler(HttpMessageHandler innerHandler)
                : base(innerHandler)
            {
            }

            protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request,
                CancellationToken cancellationToken)
            {
                Console.WriteLine("Request:");
                Console.WriteLine(request.ToString());
                if (request.Content != null)
                {
                    Console.WriteLine(await request.Content.ReadAsStringAsync());
                }

                Console.WriteLine();

                HttpResponseMessage response = await base.SendAsync(request, cancellationToken);

                Console.WriteLine("Response:");
                Console.WriteLine(response.ToString());
                if (response.Content != null)
                {
                    Console.WriteLine(await response.Content.ReadAsStringAsync());
                }

                Console.WriteLine();

                return response;
            }
        }
    }
}
 */

/*
namespace RestMemoryGame
{
    public class Configurare
    {
        [JsonProperty("id")]
        public int Id { get; set; }
        [JsonProperty("cerinta")]
        public string Cerinta { get; set; }
        
        public Configurare() {}
        
        public Configurare(int id, string cerinta)
        {
            Id = id;
            Cerinta = cerinta;
        }

        public override string ToString()
        {
            return $"[Configurare: id={Id}, cerinta={Cerinta}]";
        }
    }
    
     class Program
    {
        static HttpClient client = new HttpClient(new LoggingHandler(new HttpClientHandler()));

        private static string URL_Base = "http://localhost:8080/jucatoriGames";

        public static void Main(string[] args)
        {
            RunAsync().Wait();
        }

        static async Task RunAsync()
        {
            client.BaseAddress = new Uri(URL_Base);
            client.DefaultRequestHeaders.Accept.Clear();
            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
            
            int id = 1;
            string game = await GetJucatorAsync(URL_Base + "/game/" + id);
            if (game != null)
            { 
                Console.WriteLine("Am primit {0}", game);
            }
            else
            { 
                Console.WriteLine("No Game found with id: {0}", id);
            }
            await Task.Delay(1000);
            
            var updates = new Dictionary<string, string> { { "conf", "[xx, a, b, pix, pix, pix, pix, pix, pix, pix]" } };
            bool updated = await UpdateConfigurareAsync(URL_Base + "/configurare/" + id, updates);
            if (updated)
            {
                Console.WriteLine("Game updated successfully.");
            }
            else
            {
                Console.WriteLine("Failed to update Game.");
            }
        }

        static async Task<string> GetJucatorAsync(string path)
        {
            string result = null;
            HttpResponseMessage response = await client.GetAsync(path);
            if (response.IsSuccessStatusCode)
            {
                result = await response.Content.ReadAsStringAsync();
            }
            return result;
        }

        static async Task<bool> UpdateConfigurareAsync(string path, Dictionary<string, string> updates)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync(path, updates);
            return response.IsSuccessStatusCode;
        }
    }

    public class LoggingHandler : DelegatingHandler
    {
        public LoggingHandler(HttpMessageHandler innerHandler)
            : base(innerHandler)
        {
        }
    
        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            Console.WriteLine("Request:");
            Console.WriteLine(request.ToString());
            if (request.Content != null)
            {
                Console.WriteLine(await request.Content.ReadAsStringAsync());
            }
            Console.WriteLine();
    
            HttpResponseMessage response = await base.SendAsync(request, cancellationToken);
    
            Console.WriteLine("Response:");
            Console.WriteLine(response.ToString());
            if (response.Content != null)
            {
                Console.WriteLine(await response.Content.ReadAsStringAsync());
            }
            Console.WriteLine();
    
            return response;
        }
    }
}
*/

/*
namespace RestServices
{
    class MainClass
    {
        static HttpClient client = new HttpClient(new LoggingHandler(new HttpClientHandler()));
        
        private static string URL_Base = "http://localhost:8080/zbor";

        public static void Main(string[] args)
        {
            //Console.WriteLine("Hello World!");
            RunAsync().Wait();
        }

        static async Task RunAsync()
        {
            client.BaseAddress = new Uri(URL_Base);
            client.DefaultRequestHeaders.Accept.Clear();
            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

            
             
            
            
            Console.WriteLine("====================================================================");
            int id = 2;
            Console.WriteLine("Get Zbor with id: {0}", id);
            Zbor result = await GetZborAsync(URL_Base + "/" + id);
            if (result != null)
            {
                Console.WriteLine("Am primit {0}", result.ToString());
            }
            else
            {
                Console.WriteLine("No Zbor found with id: {0}", id);
            }
            await Task.Delay(1000);
            
             
             Console.WriteLine("====================================================================");
             Console.WriteLine("Get Zbor");
             List<Zbor> result1 = await GetTrialsAsync(URL_Base);
             foreach (var c in result1)
             {
                 Console.WriteLine("Am primit {0}", c.ToString());
             }
             await Task.Delay(1000);

             Console.WriteLine("====================================================================");
             Zbor zbor = new Zbor(10,"Oradea","Cluj","2024-05-23T23:46:18.202857400",100);
             Zbor empty = await CreateUserAsync(URL_Base, zbor);
             Console.WriteLine("Added zbor..." + empty.ToString());
             await Task.Delay(1000);
             
             Console.WriteLine("====================================================================");
             Zbor updatedZbor = new Zbor(10, "Bucuresti", "Cluj", "2024-05-23T23:46:18.202857400", 100);
             updatedZbor.id = 10; 
             Console.WriteLine("Update Zbor with id: {0}", updatedZbor.id);
             Zbor result3 = await UpdateUserAsync(URL_Base + "/" + updatedZbor.id, updatedZbor);
             //Console.WriteLine("Updated zbor..." + result3.ToString());
             await Task.Delay(1000);
             
             


             Console.WriteLine("====================================================================");
             Console.WriteLine("Delete Zbor with id: {0}", 10);
             String result2 = await DeleteTrialAsync(URL_Base+ "/" + 10);
             //Console.WriteLine("Am primit {0}", result2.ToString());
        }
        
        static async Task<String> GetTextAsync(string path)
        {
            String product = null;
            HttpResponseMessage response = await client.GetAsync(path);
            if (response.IsSuccessStatusCode)
            {
                product = await response.Content.ReadAsStringAsync();
            }
            return product;
        }
        
        static async Task<Zbor> UpdateUserAsync(string path, Zbor zbor)
        {
            Zbor result = null;
            HttpResponseMessage response = await client.PutAsJsonAsync(path, zbor);
            if (response.IsSuccessStatusCode)
            {
                result = await response.Content.ReadAsAsync<Zbor>();
            }
            return result;
        }
        
        static async Task<Zbor> GetZborAsync(string path)
        {
            Zbor product = null;
            HttpResponseMessage response = await client.GetAsync(path);
            if (response.IsSuccessStatusCode)
            {
                product = await response.Content.ReadAsAsync<Zbor>();
            }
            return product;
        }
        
        static async Task<List<Zbor>> GetTrialsAsync(string path)
        {
            List<Zbor> product = null;
            HttpResponseMessage response = await client.GetAsync(path);
            if (response.IsSuccessStatusCode)
            {
                product = await response.Content.ReadAsAsync<List<Zbor>>();
            }
            return product;
        }
        
        
        static async Task<Zbor> CreateUserAsync(string path, Zbor trial)
        {
            Zbor result = null;
            HttpResponseMessage response = await client.PostAsJsonAsync(path,trial);
            if (response.IsSuccessStatusCode)
            {
                result = await response.Content.ReadAsAsync<Zbor>();
            }
            return result;
        }
        
        static async Task<String> DeleteTrialAsync(string path)
        {
            String result = null;
            HttpResponseMessage response = await client.DeleteAsync(path);
            if (response.IsSuccessStatusCode)
            {
                result = await response.Content.ReadAsAsync<String>();
            }
            return result;
        }
        
        public class Zbor 
        {
            [JsonProperty("id")] public int id { set; get; }
            [JsonProperty("from")] public String from { set; get; }
            [JsonProperty("to")] public String to { set; get; }
            
            [JsonProperty("dataOra")] public string dataOra { set; get; }
            
            [JsonProperty("locuriDisponibile")] public int locuriDisponibile { set; get; }
            
            public Zbor() {}

            public Zbor(int id, string from, string to, string dataOra, int locuriDisponibile)
            {
                this.id = id;
                this.from = from;
                this.to = to;
                this.dataOra = dataOra;
                this.locuriDisponibile = locuriDisponibile;
            }

            public override string ToString()
            {
                return "Zbor{" +
                       "id=" + id +
                          ", from='" + from + '\'' +
                          ", to='" + to + '\'' +
                          ", dataOra=" + dataOra +
                          ", locuriDisponibile=" + locuriDisponibile +
                          '}';
            }
        }
    }
    
    public class LoggingHandler : DelegatingHandler
    {
        public LoggingHandler(HttpMessageHandler innerHandler)
            : base(innerHandler)
        {
        }
    
        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            Console.WriteLine("Request:");
            Console.WriteLine(request.ToString());
            if (request.Content != null)
            {
                Console.WriteLine(await request.Content.ReadAsStringAsync());
            }
            Console.WriteLine();
    
            HttpResponseMessage response = await base.SendAsync(request, cancellationToken);
    
            Console.WriteLine("Response:");
            Console.WriteLine(response.ToString());
            if (response.Content != null)
            {
                Console.WriteLine(await response.Content.ReadAsStringAsync());
            }
            Console.WriteLine();
    
            return response;
        }
    }
}
*/