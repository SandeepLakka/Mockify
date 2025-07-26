package io.github.sandeeplakka.mockify.generator;

import net.datafaker.Faker;
import net.datafaker.providers.base.Finance.CreditCardType;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
public final class GeneratorRegistry {

    private static final Faker faker = new Faker();
    private static final SecureRandom rnd = new SecureRandom();
    //Currently, the storage is in-memory
    private static final Map<String, DataGenerator> NO_ARG = new HashMap<>();
    private static final Map<String, java.util.function.Function<List<String>, Object>> ARG = new HashMap<>();
    private static final Pattern HEX = Pattern.compile("[0-9a-f]");

    //helpers
    private static final char[] DIGITS = "0123456789".toCharArray();
    private static final char[] LETTER = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    static {

        // Person and Auth related
        NO_ARG.put("name", () -> faker.name().fullName());
        NO_ARG.put("firstname", () -> faker.name().firstName());
        NO_ARG.put("lastname", () -> faker.name().lastName());
        NO_ARG.put("nameprefix", () -> faker.name().prefix());
        NO_ARG.put("namesuffix", () -> faker.name().suffix());
        NO_ARG.put("username", () -> faker.superhero().prefix() +
                faker.name().firstName() +
                faker.address().buildingNumber());
        NO_ARG.put("email", () -> faker.internet().emailAddress());
        NO_ARG.put("phone", () -> faker.phoneNumber().phoneNumber());
        NO_ARG.put("phoneformatted", () -> faker.phoneNumber().cellPhone());

        ARG.put("password", args -> {
            // args: lower,upper,num,special,space,len
            boolean lower = Boolean.parseBoolean(args.get(0));
            boolean upper = Boolean.parseBoolean(args.get(1));
            boolean numeric = Boolean.parseBoolean(args.get(2));
            boolean special = Boolean.parseBoolean(args.get(3));
            boolean space = Boolean.parseBoolean(args.get(4));
            int len = Integer.parseInt(args.get(5));
            String pool = "";
            if (lower) pool += "abcdefghijklmnopqrstuvwxyz";
            if (upper) pool += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            if (numeric) pool += "0123456789";
            if (special) pool += "!@#$%^&*";
            if (space) pool += " ";
            var sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                sb.append(pool.charAt(rnd.nextInt(pool.length())));
            }
            return sb.toString();
        });

        // Address related
        NO_ARG.put("city", () -> faker.address().city());
        NO_ARG.put("country", () -> faker.address().country());
        NO_ARG.put("countrycode", () -> faker.address().countryCode());
        NO_ARG.put("state", () -> faker.address().state());
        NO_ARG.put("stateabbr", () -> faker.address().stateAbbr());
        NO_ARG.put("street", () -> faker.address().streetAddress());
        NO_ARG.put("streetnumber", () -> faker.address().buildingNumber());
        NO_ARG.put("streetname", () -> faker.address().streetName());
        NO_ARG.put("streetprefix", () -> faker.address().streetPrefix());
        NO_ARG.put("streetsuffix", () -> faker.address().streetSuffix());
        NO_ARG.put("zip", () -> faker.address().zipCode());

        ARG.put("latitude", args -> {
            if (args.isEmpty()) return faker.address().latitude(); // no-arg fall-back
            double min = Double.parseDouble(args.get(0));
            double max = Double.parseDouble(args.get(1));
            return min + Math.abs(rnd.nextDouble()) % (max - min);
        });
        ARG.put("longitude", args -> {
            if (args.isEmpty()) return faker.address().longitude();
            double min = Double.parseDouble(args.get(0));
            double max = Double.parseDouble(args.get(1));
            return min + Math.abs(rnd.nextDouble()) % (max - min);
        });

        // Simple text and Numbers related
        NO_ARG.put("sentence", () -> faker.lorem().sentence());
        NO_ARG.put("paragraph", () -> faker.lorem().paragraph());
        NO_ARG.put("quote", () -> faker.hitchhikersGuideToTheGalaxy().quote());
        NO_ARG.put("uuid", () -> UUID.randomUUID().toString());
        ARG.put("price", a -> {
            double min = Double.parseDouble(a.get(0));
            double max = Double.parseDouble(a.get(1));
            double val = min + Math.abs(rnd.nextDouble()) % (max - min);
            return Math.round(val * 100.0) / 100.0;
        });

        ARG.put("int", a -> Integer.parseInt(a.get(0)) + Math.abs(rnd.nextInt()) % (Integer.parseInt(a.get(1)) - Integer.parseInt(a.get(0))));

        ARG.put("float", a -> {
            double min = Double.parseDouble(a.get(0));
            double max = Double.parseDouble(a.get(1));
            return min + Math.abs(rnd.nextDouble()) % (max - min);
        });

        // Lorem & text related
        NO_ARG.put("word", () -> faker.lorem().word());
        NO_ARG.put("question", () -> faker.lorem().sentence() + "?");
        NO_ARG.put("phrase", () -> faker.company().buzzword());
        NO_ARG.put("loremword", () -> faker.lorem().word());

        ARG.put("loremwords", a -> faker.lorem().words(randBetween(a)).stream()
                .reduce((x, y) -> x + " " + y).orElse(""));
        ARG.put("loremsentences", a -> faker.lorem().sentences(randBetween(a)).stream()
                .reduce((x, y) -> x + " " + y).orElse(""));
        ARG.put("loremparagraphs", a -> String.join("\n\n",
                faker.lorem().paragraphs(randBetween(a))));

        // Internet related
        NO_ARG.put("url", () -> faker.internet().url());
        NO_ARG.put("domainname", () -> faker.internet().domainName());
        NO_ARG.put("domainsuffix", () -> faker.internet().domainSuffix());
        NO_ARG.put("ipv4", () -> faker.internet().ipV4Address());
        NO_ARG.put("ipv6", () -> faker.internet().ipV6Address());
        NO_ARG.put("httpstatus", () -> "OK");
        NO_ARG.put("httpstatuscode", () -> 200);
        NO_ARG.put("httpmethod", () -> List.of("GET", "POST", "PUT", "DELETE")
                .get(rnd.nextInt(4)));

        // Numbers / hex / digit related
        NO_ARG.put("digit", () -> rnd.nextInt(10));
        ARG.put("digits", a -> randomDigits(Integer.parseInt(a.get(0))));

        NO_ARG.put("hex8", () -> randomHex(8));
        NO_ARG.put("hex16", () -> randomHex(16));
        NO_ARG.put("hex32", () -> randomHex(32));
        NO_ARG.put("hex128", () -> randomHex(128));
        NO_ARG.put("hex256", () -> randomHex(256));

        // Template helpers  lexify / numerify / bothify / regexp
        ARG.put("lexify", a -> faker.letterify(a.get(0)));
        ARG.put("numerify", a -> faker.numerify(a.get(0)));
        ARG.put("bothify", a -> faker.bothify(a.get(0)));
        ARG.put("regexp", a -> faker.regexify(a.get(0)));

        // oneOf* related
        ARG.put("oneofstring", a -> pickRandom(a));
        ARG.put("oneofint", a -> Integer.parseInt(pickRandom(a)));
        ARG.put("oneoffloat", a -> Double.parseDouble(pickRandom(a)));
        ARG.put("oneofdatetime", a -> pickRandom(a));

        // Date pieces / helpers related
        final String[] WEEKDAYS = {
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        final String[] MONTHS = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        NO_ARG.put("second", () -> rnd.nextInt(60));
        NO_ARG.put("minute", () -> rnd.nextInt(60));
        NO_ARG.put("hour", () -> rnd.nextInt(24));
        NO_ARG.put("day", () -> rnd.nextInt(28) + 1);
        NO_ARG.put("month", () -> rnd.nextInt(12) + 1);
        NO_ARG.put("year", () -> rnd.nextInt(2030 - 1970) + 1970);
        NO_ARG.put("weekday", () -> WEEKDAYS[rnd.nextInt(WEEKDAYS.length)]);
        NO_ARG.put("monthname", () -> MONTHS[rnd.nextInt(MONTHS.length)]);

        // Colour / Emoji / Language related
        final String[] LANG = {"in", "en", "es", "fr", "de", "it", "pt", "hi", "zh"};
        final String[] COUNTRY = {"IN", "US", "GB", "DE", "FR", "IN", "BR", "CN", "CA"};
        NO_ARG.put("hexcolor", () -> faker.color().hex());
        NO_ARG.put("safecolor", () -> faker.color().name());
        NO_ARG.put("color", () -> faker.color().name());

        NO_ARG.put("emoji", () -> faker.slackEmoji().emoji());
        NO_ARG.put("emojidescription", () -> faker.slackEmoji().emoji().describeConstable().get());

        NO_ARG.put("language", () -> faker.country().name()); // close enough
        NO_ARG.put("languageabbr", () -> faker.country().countryCode2());
        NO_ARG.put("languagebcp", () -> "%s-%s".formatted(LANG[rnd.nextInt(LANG.length)],
                COUNTRY[rnd.nextInt(COUNTRY.length)]));

        // Company / Job related
        NO_ARG.put("company", () -> faker.company().name());
        NO_ARG.put("companysuffix", () -> faker.company().suffix());
        NO_ARG.put("jobtitle", () -> faker.job().title());
        NO_ARG.put("joblevel", () -> faker.job().seniority());

        // Payment & crypto related
        NO_ARG.put("creditcardcvv", () -> faker.finance().creditCard(CreditCardType.VISA).split(" ")[2]);
        NO_ARG.put("creditcardexp", () -> faker.business().creditCardExpiry());
        NO_ARG.put("creditcardnumber", () -> faker.finance().creditCard());
        NO_ARG.put("creditcardtype", () -> "Visa");
        NO_ARG.put("currencycode", () -> faker.currency().code());
        NO_ARG.put("currencyname", () -> faker.currency().name());
        NO_ARG.put("achrouting", () -> String.format("%09d", rnd.nextInt(1_000_000_000)));
        NO_ARG.put("achaccount", () -> faker.finance().iban());
        NO_ARG.put("bitcoinaddress", () -> "1" + randomHex(33));
        NO_ARG.put("bitcoinprivatekey", () -> faker.hashing().sha256());

        // Celebrity (not me :P) related
        NO_ARG.put("celebrityactor", () -> faker.artist().name());
        NO_ARG.put("celebritybusiness", () -> faker.company().name());
        NO_ARG.put("celebritysport", () -> faker.team().name());

        ARG.put("digits n", a -> randomDigits(Integer.parseInt(a.get(0))));

        /* ──────────────────────────────────────────────────────────────
         *  digits n   (alias “digits” already exists – support literal key too)
         * ────────────────────────────────────────────────────────────── */
        ARG.put("digits n", a -> randomDigits(Integer.parseInt(a.get(0))));

        /* ──────────────────────────────────────────────────────────────
         *  bool  (50 % true)  — we already have bool[pct]; this is no-arg variant
         * ────────────────────────────────────────────────────────────── */
        NO_ARG.put("bool", () -> rnd.nextBoolean());

        /* ──────────────────────────────────────────────────────────────
         *  dateTime[start,end]  – ISO-8601 format
         * ────────────────────────────────────────────────────────────── */
        ARG.put("datetime", a -> {
            long from = java.time.Instant.parse(a.get(0)).toEpochMilli();
            long to = java.time.Instant.parse(a.get(1)).toEpochMilli();
            long t = from + Math.abs(rnd.nextLong()) % (to - from);
            return java.time.Instant.ofEpochMilli(t).toString();
        });

        /* ──────────────────────────────────────────────────────────────
         *  imageURL[w,h]  – random Picsum photo
         * ────────────────────────────────────────────────────────────── */
        ARG.put("imageurl", a -> {
            int w = Integer.parseInt(a.get(0));
            int h = Integer.parseInt(a.get(1));
            return "https://picsum.photos/%d/%d?random=%d".formatted(w, h, rnd.nextInt());
        });
    }

    {
    }

    private static String randomHex(int len) {
        var sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append(Integer.toHexString(rnd.nextInt(16)));
        return sb.toString();
    }

    // static block for easy initialization, need to change this to support custom generator plugin architecture

    private static String randomDigits(int len) {
        var sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append(DIGITS[rnd.nextInt(DIGITS.length)]);
        return sb.toString();
    }

    /* ---------- public API ------------------------------------------- */

    /**
     * Spec may be:
     * "int[1,10]"      (args between square brackets)
     * "digits 6"       (base + plain-space args)
     * "firstname"      (no args)
     */
//    public static Object generate(String spec) {
//        String work = spec.trim();
//
//        // 1) split on first '[' or first ' ' (space)
//        String base;
//        List<String> args = List.of();
//        if (work.contains("[")) {
//            base = work.substring(0, work.indexOf('['));
//            String inside = work.substring(work.indexOf('[') + 1, work.lastIndexOf(']'));
//            args = Arrays.stream(inside.split(",")).map(String::trim).toList();
//        } else if (work.contains(" ")) {
//            int pos = work.indexOf(' ');
//            base = work.substring(0, pos);
//            args = Arrays.stream(work.substring(pos + 1).trim().split(","))
//                    .map(String::trim).toList();
//        } else {
//            base = work;
//        }
//
//        String key = base.toLowerCase(Locale.ROOT);
//
//        if (args.isEmpty()) {
//            var sup = NO_ARG.get(key);
//            if (sup == null) throw unknown(spec);
//            return sup.get();
//        }
//
//        var fn = ARG.get(key);
//        if (fn == null) throw unknown(spec);
//        return fn.apply(args);
//    }
    public static Object generate(String spec) {

        String work = spec.trim();

        // --- 1) base name  (text up to first '[' or first space) ---
        int firstBracket = work.indexOf('[');
        int firstSpace = work.indexOf(' ');
        int cutPos = firstBracket == -1 ? firstSpace :
                firstSpace == -1 ? firstBracket :
                        Math.min(firstBracket, firstSpace);

        String base = (cutPos == -1) ? work : work.substring(0, cutPos);
        String key = base.toLowerCase(Locale.ROOT);

        // --- 2) extract args: everything inside the last [...] pair ---
        List<String> args = List.of();
        int lastOpen = work.lastIndexOf('[');
        int lastClose = work.lastIndexOf(']');
        if (lastOpen != -1 && lastClose > lastOpen) {
            String inside = work.substring(lastOpen + 1, lastClose);
            args = Arrays.stream(inside.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
        } else if (cutPos != -1 && work.charAt(cutPos) == ' ') {
            args = Arrays.stream(work.substring(cutPos + 1).trim().split(","))
                    .map(String::trim)
                    .toList();
        }

        // --- 3) dispatch --------------------------------------------------
        if (args.isEmpty()) {
            DataGenerator sup = NO_ARG.get(key);
            if (sup == null) throw unknown(spec);
            return sup.generate();
        }
        var fn = ARG.get(key);
        if (fn == null) throw unknown(spec);
        return fn.apply(args);
    }

    public static boolean contains(String key) {
        return NO_ARG.containsKey(key) || ARG.containsKey(key);
    }

    /* ---------- utils ------------------------------------------------ */

    private static int randBetween(List<String> range) {
        int min = Integer.parseInt(range.get(0));
        int max = (range.size() > 1 ? Integer.parseInt(range.get(1)) : min);
        return rnd.nextInt(max - min + 1) + min;
    }

    private static String pickRandom(List<String> lst) {
        return lst.get(rnd.nextInt(lst.size()));
    }

    private static IllegalArgumentException unknown(String spec) {
        return new IllegalArgumentException("Unknown generator: " + spec);
    }
}
