package de.birkenfunk.birkenbotcode.presentation.chatbot;

import opennlp.tools.doccat.*;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.*;
import opennlp.tools.util.model.ModelUtil;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ChatBot {
    /*todo Documentation*/
    final private static Map<String, String> answersToQuestions = new HashMap<>();

    private static String[] tokenizeSentence(String message) throws IOException {
        try (InputStream modelIn = new FileInputStream("./src/de.Birkenfunk.chatbot/models/en-token.bin")) {
            TokenizerME tokenizerME = new TokenizerME(new TokenizerModel(modelIn));
            return tokenizerME.tokenize(message);
        }
    }

    private static String[] lemmatizeTokens(String[] tokens, String[] posTags) throws IOException {
        try (InputStream modelIn = new FileInputStream("./src/de.Birkenfunk.chatbot/models/en-lemmatizer.bin")) {
            LemmatizerME lemmatizerME = new LemmatizerME(new LemmatizerModel(modelIn));
            return lemmatizerME.lemmatize(tokens, posTags);
        }
    }

    private static String[] detectPOSTags(String[] tokens) throws IOException {
        try (InputStream modelIn = new FileInputStream("./src/de.Birkenfunk.chatbot/models/en-pos-maxent.bin")) {
            POSTaggerME posTaggerME = new POSTaggerME(new POSModel(modelIn));
            return posTaggerME.tag(tokens);
        }
    }

    private static String detectCategory(DoccatModel model, String[] finalTokens) {
        DocumentCategorizerME documentCategorizerME = new DocumentCategorizerME(model);
        double[] probabilitiesOfOutcomes = documentCategorizerME.categorize(finalTokens);
        return documentCategorizerME.getBestCategory(probabilitiesOfOutcomes);
    }

    private static DoccatModel trainCategorizerModel() throws IOException {
        InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(new File("./src/de.Birkenfunk.chatbot/faq-categorizer.txt"));
        ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
        DoccatFactory factory = new DoccatFactory(new FeatureGenerator[] { new BagOfWordsFeatureGenerator() });
        TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
        params.put(TrainingParameters.CUTOFF_PARAM, 0);
        return DocumentCategorizerME.train("en", sampleStream, params, factory);
    }

    private static void fillAnswersHashmap() {
        answersToQuestions.put("greeting", "Hello, how can I help you?");
        answersToQuestions.put("need-help", "I can not give you any help :(");
        answersToQuestions.put("conversation-continue", "What else can I help you with?");
        answersToQuestions.put("conversation-complete", "Nice chatting with you. Bbye.");
    }

    public static String getResponse(String message) {
        fillAnswersHashmap();
        try {
            DoccatModel model = trainCategorizerModel();
            String[] tokens = tokenizeSentence(message);
            String[] posTags = detectPOSTags(tokens);
            String[] lemmas = lemmatizeTokens(tokens, posTags);
            String category = detectCategory(model, lemmas);
            return answersToQuestions.get(category);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
