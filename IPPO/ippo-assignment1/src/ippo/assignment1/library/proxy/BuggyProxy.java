// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.proxy;

import ippo.assignment1.library.proxy.BuggyProxy;
import ippo.assignment1.library.Picture;
import ippo.assignment1.library.service.Service;
import ippo.assignment1.library.service.ServiceFromProperties;
import ippo.assignment1.library.utils.Properties;
import ippo.assignment1.library.utils.BugChooser;
import ippo.assignment1.library.utils.HashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javafx.scene.image.Image;

/**
 * A version of the CacheProxy service for the PictureViewer application which
 * simulates various bugs.
 * <p>
 * The following properties are supported:
 * <ul>
 * <li><code>proxy.buggy.service</code> - the class to use for the base
 * service if not specified in the constructor.</li>
 * <li><code>proxy.debug</code> - <code>true</code> or <code>false</code>
 * to enable/disable debugging.</li>
 * </ul>
 * <p>
 * The following environment variables are supported:
 * <ul>
 * <li><code>IPPO_BUG</code> and <code>IPPO_UID</code> - used in combination
 * to compute the bug to exhibit.</li>
 * </ul>
 *
 * @author Michael Glienecke (adapted from a previous version by Paul Anderson)
 * @version §VERSION, §PUBDATE
 */

/*
 * The exhibited bug depends on the BugID.
 * This is normally computed from the student ID and the "bug" (letter)
 * But the bugID can be explicitly forced with the environment variable IPPO_BUGID
 * (eg. for testing, automarking)
 *
 * We don't want to declare the BugIDs with symbolic names
 * because the symbols may expose the meaning
 *
 * See the file AllBugsProxyTest for a description of the bugs, and symbolic names
 */

public class BuggyProxy implements Service {

    private Service baseService = null;
    private final HashMap<String, Picture> cacheMap = new HashMap<String, Picture>();
    private Boolean debugging = false;
    private int bugID;
    private String bugToIssue = "NONE";
    private String uuid = "NONE";
    private static final Random rand = new Random();
    private static String lastKey = null;
    private static int count = 0;
    private static int brokenIndex = 0;
    public static int explicitBugID = -1;
    private static int alreadySlept = 0;
    private static int timeout = 45;

    /**
     * Return a textual name for the service.
     *
     * @return the name of the base service
     */
    public String serviceName() {
        return baseService.serviceName();
    }

    /**
     * Create a cache proxy service based on the service specified in the
     * <code>BuggyProxy.base_service</code> resource.
     */
    public BuggyProxy() {
        baseService = new ServiceFromProperties("proxy.buggy.service");
        init();
    }


    /**
     * An overloaded constructor which takes the bug to generate.
     * This would be used if no build system like gradle, etc. is in place so direct bugs can be generated
     *
     * @param baseService the base service
     * @param uuid        the UoE ID
     * @param bug         the bug which shall be issued ("A", "B", ...)
     */
    public BuggyProxy(Service baseService, String uuid, String bug) {
        this.baseService = baseService;
        this.bugToIssue = bug;
        this.uuid = uuid;
        init();
    }

    private void init() {

        debugging = Properties.getBool("proxy.debug");

        // one of the bugs makes things hang
        // we would like the student to detect this
        // but if they don't, we don't want to hang forever!
        // so we give up after this many seconds (default above)
        // but if we are developing, we don't want to wait this long for every test!
        // so you can override it from the environment:
        String ts = System.getenv("IPPO_TIMEOUT");
        if (ts != null) {
            if (Pattern.matches("[0-9]+", ts)) {
                timeout = Integer.parseInt(ts);
                if (debugging) {
                    System.err.println("[debug] BuggyProxy: using timeout from ENV[IPPO_TIMEOUT]: " + timeout);
                }
            } else {
                System.err.println("invalid timeout: '" + ts + "'");
                throw new RuntimeException("invalid tineout: '" + ts + "'");
            }
        }

        // when we are running tests, we want the test framework to
        // be able to explicitly set the bug ID
        if (explicitBugID >= 0) {
            bugID = explicitBugID;
            System.err.println("[debug] BuggyProxy: using explicit bugID:" + bugID);
        } else {


            // sanity check
            if (uuid == null || uuid.equals("") || uuid.equals("NONE")) {
                System.err.println("you must set 'UID' to your UUN in the build.gradle file\n");
                throw new RuntimeException("you must set 'UID' to your UUN in the build.gradle file");
            }

            // no bug
            if (bugToIssue == null || bugToIssue.equals("") || bugToIssue.equals("NONE")) {
                bugID = 0;
            } else {
                // get the uid & bug
                System.out.println("BuggyProxy: student id=" + uuid + ", simulating bug '" + bugToIssue + "'");

                // find an bugID based on the id of the user and the bug
                BugChooser errorChooser = new BugChooser(uuid);
                bugID = errorChooser.errorForCategory(bugToIssue);
            }
        }
    }

    /**
     * Return a picture from the base service. If a picture with the same
     * subject and index has previously been returned, the picture will be
     * obtained from the cache, otherwise it will be fetched from the base
     * service (and inserted into the cache).
     *
     * @param subject the free-text subject string
     * @param index   the index of the matching picture to return
     * @return the requested picture
     */
    public Picture getPicture(String subject, int index) {
        String key = subject + "/" + index;
        if (bugID == 2) {
            key = "picture/1";
        }
        if (bugID == 14) {
            key = subject;
        }
        Picture picture = cacheMap.get(key);

        count = count + 1;
        if (bugID == 12 && count > 5) {
            count = 1 / 0;
        }

        if (picture != null) {
            return picture;
        } else {
            // hang when inserting 2nd picture into cache
            // give the user some time to decide if they are going to interrupt
            // then assume that they aren't going to do anything about it
            // only delay the first time, so we aren't hanging about for ages!
            if (alreadySlept == 0 && bugID == 15 && cacheMap.size() >= 1) {
                int i = 0;
                while (i < timeout) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    ++i;
                    System.out.println("sleeping ... " + i);
                }
                ++alreadySlept;
            }
            // corrupt something when we put the 4th picture in the cache
            if (bugID == 5 && cacheMap.size() == 2) {
                List<String> keys = new ArrayList<String>(cacheMap.keySet());
                String randomKey = keys.get(rand.nextInt(cacheMap.size()));
                picture = baseService.getPicture(subject, index);
                Picture corruptPicture = new Picture((Image) null, picture.subject(), picture.source(), picture.index());
                cacheMap.put(randomKey, corruptPicture);
            } else if (bugID == 6) {
                // don't call the base service - return an empty picture
                picture = new Picture((Image) null, subject, "NoService", index);
            } else if (bugID == 7) {
                // call the base service but return an empty picture
                picture = baseService.getPicture(subject, index);
                picture = new Picture((Image) null, subject, "NoService", index);
            } else if (bugID == 9) {
                // swap picture with last one in cache
                picture = baseService.getPicture(subject, index);
                if (lastKey != null) {
                    Picture lastPicture = cacheMap.get(lastKey);
                    cacheMap.put(key, lastPicture);
                    String tmpKey = lastKey;
                    lastKey = key;
                    key = tmpKey;
                } else {
                    lastKey = key;
                }
            } else {
                // correct - fetch the picture
                picture = baseService.getPicture(subject, index);
            }

            if (bugID != 8) {
                cacheMap.put(key, picture);
            }
            if (bugID == 3) {
                picture.setSubject("");
            }
            if (bugID == 4) {
                picture.setIndex(0);
            }
            if (bugID == 13) {
                picture.setIndex(++brokenIndex);
            }
            if (bugID == 10 && cacheMap.size() > 2) {
                picture.setSubject("");
            }
            if (bugID == 11 && cacheMap.size() > 2) {
                picture.setIndex(0);
            }

            return picture;
        }
    }
}
