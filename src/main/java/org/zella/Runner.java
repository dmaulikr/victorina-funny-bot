package org.zella;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.slf4j.LoggerFactory;
import org.zella.config.IConfig;
import org.zella.config.impl.Config;
import org.zella.db.IAnswersDao;
import org.zella.db.IBotDao;
import org.zella.db.IPhrasesDao;
import org.zella.db.impl.AnswerDao;
import org.zella.db.impl.BotDao;
import org.zella.db.impl.PhrasesDao;
import org.zella.supervisor.BotSupervisorActor;

/**
 * @author zella.
 */
public class Runner {


    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {

        IConfig config = new Config();
        IPhrasesDao phrasesDao = new PhrasesDao();
        IBotDao botDao = new BotDao();
        IAnswersDao answersDao = new AnswerDao();

        String joke = phrasesDao.randomPhraseByType("joke");

        log.info("Database works?, see random joke: {}", joke);

        // Create the actor system
        final ActorSystem system = ActorSystem.create("bot_system");
        log.info("ActorSystem started!");
        ActorRef botSupervisor = system.actorOf(BotSupervisorActor.props(config, phrasesDao, answersDao, botDao));

        log.info("BotSupervisorActor created!");

    }
}
