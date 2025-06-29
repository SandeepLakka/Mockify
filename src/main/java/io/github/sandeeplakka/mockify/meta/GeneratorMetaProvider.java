package io.github.sandeeplakka.mockify.meta;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Catalogue of every generator the system understands.
 * The UI displays the {@code label}; the YAML uses {@code name}.
 */
//TODO: make it more generic and to support plugin architecture for custom defined generators.
@Component
public class GeneratorMetaProvider {

    private static final List<Meta> CATALOG = List.of(
            /* ───────────── Person ───────────── */
            new Meta("firstName", "First name", "", "firstName"),
            new Meta("lastName", "Last name", "", "lastName"),
            new Meta("name", "Full name", "", "name"),
            new Meta("namePrefix", "Name prefix", "", "namePrefix"),
            new Meta("nameSuffix", "Name suffix", "", "nameSuffix"),
            new Meta("email", "E-mail", "", "email"),
            new Meta("phone", "Phone", "", "phone"),
            new Meta("phoneFormatted", "Phone (formatted)", "", "phoneFormatted"),
            new Meta("username", "Username", "", "username"),
            new Meta("password[lower,upper,numeric,special,space,length]",
                    "Password", "flags,length", "password[true,true,true,true,false,12]"),

            /* ───────────── Address ──────────── */
            new Meta("city", "City", "", "city"),
            new Meta("country", "Country", "", "country"),
            new Meta("countryCode", "Country code", "", "countryCode"),
            new Meta("state", "State/Province", "", "state"),
            new Meta("stateAbbr", "State abbr", "", "stateAbbr"),
            new Meta("street", "Street", "", "street"),
            new Meta("streetNumber", "Street number", "", "streetNumber"),
            new Meta("streetName", "Street name", "", "streetName"),
            new Meta("streetPrefix", "Street prefix", "", "streetPrefix"),
            new Meta("streetSuffix", "Street suffix", "", "streetSuffix"),
            new Meta("zip", "ZIP / Post code", "", "zip"),
            new Meta("latitude[min,max]", "Latitude", "min,max", "latitude[-90,90]"),
            new Meta("longitude[min,max]", "Longitude", "min,max", "longitude[-180,180]"),

            /* ───────────── Lorem / Text ─────── */
            new Meta("word", "Single word", "", "word"),
            new Meta("sentence", "Sentence", "", "sentence"),
            new Meta("paragraph", "Paragraph", "", "paragraph"),
            new Meta("question", "Question", "", "question"),
            new Meta("quote", "Quote", "", "quote"),
            new Meta("phrase", "Catch-phrase", "", "phrase"),
            new Meta("loremWord", "Lorem word", "", "loremWord"),
            new Meta("loremWords[min,max]", "Lorem words (range)", "min,max", "loremWords[3,7]"),
            new Meta("loremSentences[min,max]",
                    "Lorem sentences (range)", "min,max", "loremSentences[1,2]"),
            new Meta("loremParagraphs[min,max]",
                    "Lorem paragraphs (range)", "min,max", "loremParagraphs[1]"),

            /* ───────────── Internet ─────────── */
            new Meta("URL", "URL", "", "URL"),
            new Meta("domainName", "Domain name", "", "domainName"),
            new Meta("domainSuffix", "Domain suffix", "", "domainSuffix"),
            new Meta("IPv4", "IPv4", "", "IPv4"),
            new Meta("IPv6", "IPv6", "", "IPv6"),
            new Meta("HTTPStatus", "HTTP status", "", "HTTPStatus"),
            new Meta("HTTPStatusCode", "HTTP status code", "", "HTTPStatusCode"),
            new Meta("HTTPMethod", "HTTP method", "", "HTTPMethod"),
            new Meta("UUID", "UUID", "", "UUID"),

            /* ───────────── Numbers & Bool ───── */
            new Meta("price[min,max]", "Price", "min,max", "price[10,199]"),
            new Meta("int[min,max]", "Integer", "min,max", "int[1,100]"),
            new Meta("float[min,max]", "Float", "min,max", "float[0.1,9.9]"),
            new Meta("digit", "Single digit", "", "digit"),
            new Meta("digits n", "Digits (n length)", "n", "digits 6"),
            new Meta("hex8", "Hex-8", "", "hex8"),
            new Meta("hex16", "Hex-16", "", "hex16"),
            new Meta("hex32", "Hex-32", "", "hex32"),
            new Meta("hex128", "Hex-128", "", "hex128"),
            new Meta("hex256", "Hex-256", "", "hex256"),
            new Meta("bool", "Boolean (50 %)", "", "bool"),
            new Meta("bool[pct]", "Boolean with probability", "0-100", "bool[80]"),

            /* ───────────── Templates / RegExp ─ */
            new Meta("lexify", "Lexify pattern", "pattern", "lexify(\"ITEM-????\")"),
            new Meta("numerify", "Numerify pattern", "pattern", "numerify(\"####-2025\")"),
            new Meta("bothify", "Bothify pattern", "pattern", "bothify(\"??-##\")"),
            new Meta("regexp", "RegExp", "regex", "regexp(\"[A-Z]{2}[0-9]{3}\")"),

            /* ───────────── oneOf / choice ───── */
            new Meta("oneOfString", "Pick one (string list)", "list", "oneOfString"),
            new Meta("oneOfInt", "Pick one (int list)", "list", "oneOfInt"),
            new Meta("oneOfFloat", "Pick one (float list)", "list", "oneOfFloat"),
            new Meta("oneOfDateTime", "Pick one (ISO date list)", "list", "oneOfDateTime"),

            /* ───────────── Date / Time ──────── */
            new Meta("dateTime[start,end]", "DateTime range", "start,end", "dateTime[2000-01-01T00:00:00Z,2025-01-01T00:00:00Z]"),
            new Meta("second", "Second (0-59)", "", "second"),
            new Meta("minute", "Minute (0-59)", "", "minute"),
            new Meta("hour", "Hour (0-23)", "", "hour"),
            new Meta("day", "Day (1-31)", "", "day"),
            new Meta("month", "Month (1-12)", "", "month"),
            new Meta("year", "Year", "", "year"),
            new Meta("weekDay", "Week day", "", "weekDay"),
            new Meta("monthName", "Month name", "", "monthName"),

            /* ───────────── Media ────────────── */
            new Meta("imageURL[w,h]", "Image URL (random)", "width,height", "imageURL[300,300]"),

            /* ───────────── Colour & Emoji ───── */
            new Meta("color", "Colour name", "", "color"),
            new Meta("hexColor", "Colour HEX", "", "hexColor"),
//        new Meta("RGBColor",           "Colour RGB()",                   "",                 "RGBColor"),
            new Meta("safeColor", "Safe colour", "", "safeColor"),
            new Meta("emoji", "Emoji", "", "emoji"),
            new Meta("emojiDescription", "Emoji description", "", "emojiDescription"),

            /* ───────────── Language & Region ─ */
            new Meta("language", "Language", "", "language"),
            new Meta("languageAbbr", "Language abbr", "", "languageAbbr"),
            new Meta("languageBCP", "Language BCP-47", "", "languageBCP"),

            /* ───────────── Company / Job ───── */
            new Meta("company", "Company", "", "company"),
            new Meta("companySuffix", "Company suffix", "", "companySuffix"),
            new Meta("jobTitle", "Job title", "", "jobTitle"),
            new Meta("jobLevel", "Job level", "", "jobLevel"),

            /* ───────────── Money / Crypto ──── */
            new Meta("creditCardCVV", "CC CVV", "", "creditCardCVV"),
            new Meta("creditCardExp", "CC expiry", "", "creditCardExp"),
            new Meta("creditCardNumber", "CC number", "", "creditCardNumber"),
            new Meta("creditCardType", "CC type", "", "creditCardType"),
            new Meta("currencyCode", "Currency code", "", "currencyCode"),
            new Meta("currencyName", "Currency name", "", "currencyName"),
            new Meta("ACHRouting", "ACH routing", "", "ACHRouting"),
            new Meta("ACHAccount", "ACH account", "", "ACHAccount"),
            new Meta("bitcoinAddress", "Bitcoin address", "", "bitcoinAddress"),
            new Meta("bitcoinPrivateKey", "Bitcoin private key", "", "bitcoinPrivateKey"),

            /* ───────────── Celebrity ───────── */
            new Meta("celebrityActor", "Celebrity (actor)", "", "celebrityActor"),
            new Meta("celebrityBusiness", "Celebrity (business)", "", "celebrityBusiness"),
            new Meta("celebritySport", "Celebrity (sport)", "", "celebritySport")
    );

    public List<Meta> all() {
        return CATALOG;
    }

    public record Meta(String name, String label, String args, String example) {
    }
}
