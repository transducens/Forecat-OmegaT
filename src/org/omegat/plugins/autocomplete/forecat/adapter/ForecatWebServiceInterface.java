package org.omegat.plugins.autocomplete.forecat.adapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.miniforecat.languages.LanguagesInput;
import org.miniforecat.languages.LanguagesOutput;
import org.miniforecat.selection.SelectionInput;
import org.miniforecat.selection.SelectionOutput;
import org.miniforecat.suggestions.SuggestionsInput;
import org.miniforecat.suggestions.SuggestionsOutput;
import org.miniforecat.translation.TranslationInput;
import org.miniforecat.translation.TranslationOutput;

public class ForecatWebServiceInterface extends IForecatInterface {

	public static String BASEURL = "http://forecat-970.appspot.com/rest/services/";

	public ForecatWebServiceInterface()
	{
		iface = this;
	}
	
	@Override
	public List<LanguagesOutput> getLanguages(
			ArrayList<LanguagesInput> inputLanguagesList) {
		
		ArrayList<LanguagesOutput> ret = new ArrayList<LanguagesOutput>();
		try {
			String url = BASEURL + "languagesService?";

			for (LanguagesInput li : inputLanguagesList) {
				url += "&engine=" + li.getEngine();
				url += "&key=" + li.getKey();
			}

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			if (con.getResponseCode() != 200) {
				throw new Exception("Error code " + con.getResponseCode()
						+ " on request " + url);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONArray jarray = new JSONArray(response.toString());
			JSONObject jo;
			for (int i = 0; i < jarray.length(); i++) {
				jo = jarray.getJSONObject(i);

				ret.add(new LanguagesOutput(jo.getString("engine"), jo
						.getString("sourceName"), jo.getString("sourceCode"),
						jo.getString("targetName"), jo.getString("targetCode")));

			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			return null;
		}

		return ret;
	}

	@Override
	public TranslationOutput translate(TranslationInput inputTranslation) {
		TranslationOutput ret = null;
		try {
			String url = BASEURL + "translationService?" + "sourceText="
					+ URLEncoder.encode(inputTranslation.getSourceText(), "UTF-8") + "&sourceCode="
					+ inputTranslation.getSourceCode() + "&targetCode="
					+ inputTranslation.getTargetCode() + "&maxLength=4&minLength=1";

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			if (con.getResponseCode() != 200) {
				throw new Exception("Error code " + con.getResponseCode()
						+ " on request " + url);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONObject jo = new JSONObject(response.toString());

			ret = new TranslationOutput(jo.getInt("numberSegments"),
					jo.getInt("numberSegments"));
			
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			return null;
		}

		return ret;
	}

	@Override
	public List<SuggestionsOutput> getSuggestions(
			SuggestionsInput inputSuggestions) {
		List<SuggestionsOutput> ret = new ArrayList<SuggestionsOutput>();
		try {
			String url = BASEURL + "suggestionService?" + "targetText="
					+ URLEncoder.encode(inputSuggestions.getTargetText(), "UTF-8") + "&prefixText="
					+ URLEncoder.encode(inputSuggestions.getPrefixText(), "UTF-8") + "&position="
					+ inputSuggestions.getPosition();

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			if (con.getResponseCode() != 200) {
				throw new Exception("Error code " + con.getResponseCode()
						+ " on request " + url);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONArray jarray = new JSONArray(response.toString());
			JSONObject jo;
			for (int i = 0; i < jarray.length(); i++) {
				jo = jarray.getJSONObject(i);

				ret.add(new SuggestionsOutput(jo.getString("suggestionText"),
						jo.getDouble("suggestionFeasibility"), jo
								.getString("id"), jo.getInt("position"), jo
								.getInt("numberWords")));
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			return null;
		}

		return ret;
	}

	@Override
	public SelectionOutput select(SelectionInput inputSelection) {
		SelectionOutput ret = null;
		try {
			String url = BASEURL + "selectionService?" + "text="
					+ inputSelection.getId() + "&position="
					+ inputSelection.getPosition();

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			if (con.getResponseCode() != 200) {
				throw new Exception("Error code " + con.getResponseCode()
						+ " on request " + url);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONObject jo = new JSONObject(response.toString());

			ret = new SelectionOutput(jo.getInt("numberSegments"));

		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			return null;
		}

		return ret;
	}

}
