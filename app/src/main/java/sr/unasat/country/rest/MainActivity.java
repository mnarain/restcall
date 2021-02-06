package sr.unasat.country.rest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sr.unasat.country.rest.dto.CountryDto;

public class MainActivity extends AppCompatActivity {
    private final String ALL_COUNTRIES = "https://restcountries.eu/rest/v2/all";
    private TextView land;
    private TextView hoofdstad;
    private TextView regio;
    private Spinner countrySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        land = (TextView) findViewById(R.id.land);
        hoofdstad = (TextView) findViewById(R.id.hoofdstad);
        regio = (TextView) findViewById(R.id.regio);
        countrySpinner = (Spinner) findViewById(R.id.countryd_dropdown);
        initSpinnerListener();
        getCountryList();
    }

    private void initSpinnerListener() {
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                CountryDto selectedCountry = (CountryDto) ((Spinner) findViewById(R.id.countryd_dropdown)).getSelectedItem();
                loadCountryDetails(selectedCountry);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getApplicationContext(), "U heeft geen selectie gemaakt!", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void loadCountryDetails(CountryDto selectedCountry) {
        land.setText(selectedCountry.getName());
        hoofdstad.setText(selectedCountry.getCapital());
        regio.setText(selectedCountry.getRegion());
    }

    private void getCountryList() {
        String URL = ALL_COUNTRIES;
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //final String response = COUNTRIES_JSON_DATA;
                        List<CountryDto> countryList = mapJsonToCountryObject(response);
                        // System.out.println(countryList);
                        ArrayAdapter<CountryDto> dataAdapter = new ArrayAdapter<CountryDto>(getApplicationContext(),
                                android.R.layout.simple_spinner_item,
                                countryList);

                        // Drop down layout style - list view
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // attaching data adapter to spinner
                        countrySpinner.setAdapter(dataAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private List<CountryDto> mapJsonToCountryObject(String jsonArray) {
        ObjectMapper mapper = new ObjectMapper();
        List<CountryDto> countryList = new ArrayList<>();
        List<Map<String, ?>> countryArray = null;
        CountryDto country = null;

        try {
            countryArray = mapper.readValue(jsonArray, List.class);
            for (Map<String, ?> map : countryArray) {
                country = new CountryDto((String) map.get("name"), (String) map.get("capital"), (String) map.get("region"));
                countryList.add(country);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Er is wat fout gegaan bij het parsen van de json data");
        }
        return countryList;
    }

    private final String COUNTRIES_JSON_DATA = "[{\"name\":\"Suriname\",\"topLevelDomain\":[\".sr\"],\"alpha2Code\":\"SR\",\"alpha3Code\":\"SUR\",\"callingCodes\":[\"597\"],\"capital\":\"Paramaribo\",\"altSpellings\":[\"SR\",\"Sarnam\",\"Sranangron\",\"Republic of Suriname\",\"Republiek Suriname\"],\"region\":\"Americas\",\"subregion\":\"South America\",\"population\":541638,\"latlng\":[4.0,-56.0],\"demonym\":\"Surinamer\",\"area\":163820.0,\"gini\":52.9,\"timezones\":[\"UTC-03:00\"],\"borders\":[\"BRA\",\"GUF\",\"FRA\",\"GUY\"],\"nativeName\":\"Suriname\",\"numericCode\":\"740\",\"currencies\":[{\"code\":\"SRD\",\"name\":\"Surinamese dollar\",\"symbol\":\"$\"}],\"languages\":[{\"iso639_1\":\"nl\",\"iso639_2\":\"nld\",\"name\":\"Dutch\",\"nativeName\":\"Nederlands\"}],\"translations\":{\"de\":\"Suriname\",\"es\":\"Surinam\",\"fr\":\"Surinam\",\"ja\":\"スリナム\",\"it\":\"Suriname\",\"br\":\"Suriname\",\"pt\":\"Suriname\",\"nl\":\"Suriname\",\"hr\":\"Surinam\",\"fa\":\"سورینام\"},\"flag\":\"https://restcountries.eu/data/sur.svg\",\"regionalBlocs\":[{\"acronym\":\"CARICOM\",\"name\":\"Caribbean Community\",\"otherAcronyms\":[],\"otherNames\":[\"Comunidad del Caribe\",\"Communauté Caribéenne\",\"Caribische Gemeenschap\"]},{\"acronym\":\"USAN\",\"name\":\"Union of South American Nations\",\"otherAcronyms\":[\"UNASUR\",\"UNASUL\",\"UZAN\"],\"otherNames\":[\"Unión de Naciones Suramericanas\",\"União de Nações Sul-Americanas\",\"Unie van Zuid-Amerikaanse Naties\",\"South American Union\"]}],\"cioc\":\"SUR\"},{\"name\":\"Netherlands\",\"topLevelDomain\":[\".nl\"],\"alpha2Code\":\"NL\",\"alpha3Code\":\"NLD\",\"callingCodes\":[\"31\"],\"capital\":\"Amsterdam\",\"altSpellings\":[\"NL\",\"Holland\",\"Nederland\"],\"region\":\"Europe\",\"subregion\":\"Western Europe\",\"population\":17019800,\"latlng\":[52.5,5.75],\"demonym\":\"Dutch\",\"area\":41850.0,\"gini\":30.9,\"timezones\":[\"UTC-04:00\",\"UTC+01:00\"],\"borders\":[\"BEL\",\"DEU\"],\"nativeName\":\"Nederland\",\"numericCode\":\"528\",\"currencies\":[{\"code\":\"EUR\",\"name\":\"Euro\",\"symbol\":\"€\"}],\"languages\":[{\"iso639_1\":\"nl\",\"iso639_2\":\"nld\",\"name\":\"Dutch\",\"nativeName\":\"Nederlands\"}],\"translations\":{\"de\":\"Niederlande\",\"es\":\"Países Bajos\",\"fr\":\"Pays-Bas\",\"ja\":\"オランダ\",\"it\":\"Paesi Bassi\",\"br\":\"Holanda\",\"pt\":\"Países Baixos\",\"nl\":\"Nederland\",\"hr\":\"Nizozemska\",\"fa\":\"پادشاهی هلند\"},\"flag\":\"https://restcountries.eu/data/nld.svg\",\"regionalBlocs\":[{\"acronym\":\"EU\",\"name\":\"European Union\",\"otherAcronyms\":[],\"otherNames\":[]}],\"cioc\":\"NED\"}]";
}