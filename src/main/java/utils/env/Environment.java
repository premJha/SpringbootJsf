package utils.env;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/*import com.hcm.ApplicationConstants;
import com.hcm.model.Resource;
import com.hcm.wf.WorkflowSchedule;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import utils.CommandExecutor;
import utils.PortForwarder;
import utils.PortForwarderImpl;
import utils.kubernetes.Pod;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;*/
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Environment {
    private String name=null;
    private String plane;

    private static String envPrefix;

 /*   public String getAllWorkflows(String app, String tenant) {
        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        Client client = ClientBuilder.newClient(config);
        String url = "http://localhost:8082/workflow/api/v2/schedules";
        String namespace = app + "-" + tenant;
        url = url + "?namespace=" + namespace;

        WebTarget target = client.target(url);
        final Invocation.Builder builder = target.request().accept(MediaType.APPLICATION_JSON)
                .header("Content-type", "application/json")
                .header("Accept-Charset", "UTF-8");
        final String s = builder.get(String.class);
        return s;
    }*/



  /*  public List<WorkflowSchedule> getWorkflowSchedules(String app, String tenant) {
         List<WorkflowSchedule> workflowSchedules=null;
        setPlane("dp");
        String appName;
        if(app.toUpperCase().equals("HCM")){
            appName = "hcm-cm";
        }else {
            appName = "hcm-talent";

        }
        final String portForwardCommand = ApplicationConstants.PORT_FORWARD_COMMAND_MAP.get("WORKFLOW_SCHEDULER_SWAGGER");
        final PortForwarderImpl portForwarder = new PortForwarderImpl();

        try {
            final boolean b = portForwarder.portForward(portForwardCommand, getEnvironmentVars());
            final String allWorkflows = getAllWorkflows(appName, tenant);
            workflowSchedules = convertToWorkflowSchedules(allWorkflows);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return workflowSchedules;
    }*/

  /*  public List<WorkflowSchedule> convertToWorkflowSchedules(String json) {
        List<WorkflowSchedule> workflowSchedulesList = null;
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            final WorkflowSchedule[] workflowSchedules = objectMapper.readValue(json, WorkflowSchedule[].class);
            workflowSchedulesList = Arrays.asList(workflowSchedules);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return workflowSchedulesList;
    }*/

    public String getRegionName() throws Exception{
        String regionName = null;
        if (name == null) {
            throw new Exception("name not set. create Environment with region in constructor");
        }
        String envHome=System.getenv("ENV_ACCESS_HOME");
        if (envHome == null) {
            throw new Exception("Please set ENV_ACCESS_HOME environment variable pointing to environment-access");

        }
        String cpKubeconfig=envHome+File.separator+name+File.separator+"cp-kubeconfig";
        List<String> lines = Files.readAllLines(Paths.get(cpKubeconfig));
        int lineCount=0;
        for (String line : lines) {
            lineCount++;
            if(line.contains("--region"))
                break;
        }
        regionName=lines.get(lineCount);
        String pattern="\\s*-\\s*(.*)";
        Pattern pattern1 = Pattern.compile(pattern);
        Matcher matcher = pattern1.matcher(regionName);
        if (matcher.matches()) {
            regionName= matcher.group(1);
        }
        return regionName;
    }

    public List<String> getProfiles() {
        List<String> profiles=new ArrayList<>();
        String regExp="\\[(.*)\\]";
        Pattern pattern = Pattern.compile(regExp);

        String userHome = System.getProperty("user.home");
        String file = userHome+ "/.oci/config";
        try {
            List<String> lines = Files.readAllLines(Paths.get(file));

            lines.forEach(p->{
                Matcher matcher = pattern.matcher(p);
                if (matcher.matches()) {
                    profiles.add(matcher.group(1));
                }
            });

            System.out.println(profiles.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return profiles;
    }

    @Override
    public String toString() {
        return "name:"+name+" plane:"+plane;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlane() {
        return plane;
    }

    public void setPlane(String plane) {
        this.plane = plane;
    }

    public type getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(type envsironmentType) {
        this.environmentType = envsironmentType;
    }

/*    public void portForwardFeatureManager() {
        setPlane("dp");
        String portForwardCommand = ApplicationConstants.PORT_FORWARD_COMMAND_MAP.get("FEATURE_MANAGER_SWAGGER");
        final PortForwarderImpl portForwarder = new PortForwarderImpl();
        try {
            portForwarder.portForward(portForwardCommand, this.getEnvironmentVars());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }*/
 /*   public List<Resource> getActiveTenantsAndResources(List<String> resources) {
        System.out.println("Getting Active tenants for "+this.getName());
        List<Resource> tenantsAndResources = new ArrayList<>();
        PortForwarderImpl portForwarder = new PortForwarderImpl();

        String portForwardCommand = ApplicationConstants.PORT_FORWARD_COMMAND_MAP.get("RSM");

        boolean portForwarded = false;
        try {
            portForwarded = portForwarder.portForward(portForwardCommand,this.getEnvironmentVars());
            String endpoint="http://localhost:8080/rsm/maintenance/activeTenantsData";
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(endpoint);
            MediaType TEXT_CSV_TYPE = new MediaType("text", "csv");

            Response response = target.request(TEXT_CSV_TYPE).get();
            final CSVReader reader = new CSVReader(new StringReader(response.readEntity(String.class)));
            AtomicInteger n=new AtomicInteger();
            AtomicInteger nullCount=new AtomicInteger();
            String[] nextRecord;

            try {
                while ((nextRecord = reader.readNext()) != null) {
                    final Resource resource = Resource.convertToResource(nextRecord);
                    if (resource != null) {
                        tenantsAndResources.add(resource);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
           e.printStackTrace();
        }

//        System.out.println("found:"+tenantsAndResources.size());
        return tenantsAndResources;

    }*/

    public enum type {DEV,PROD};// dev or prod

    private type environmentType =type.PROD;

    public static List<String> getRegions() throws Exception{
        String envHome=System.getenv("ENV_ACCESS_HOME");
        if (envHome == null) {
            throw new Exception("Please set ENV_ACCESS_HOME environment variable pointing to environment-access");

        }
        File envHomeFolder = new File(envHome);
        List<String > regions = Files.list(Paths.get(envHome)).
                filter(p -> p.toFile().getName().startsWith(envPrefix)).
                filter(p -> new File(p.toUri()).isDirectory()).
                map(p->p.toFile().getName()).
                collect(Collectors.toList());

        return regions;

    }

    public static List<String> getAllDevEnvironments() throws Exception {
        List<String> devEnvironments = new ArrayList<>();
        List<String> allEnvironments = getAllEnvironments();
        allEnvironments.forEach(e->{
            if(e.startsWith("aiapps0")){
                devEnvironments.add(e);
            }
        });

        return devEnvironments;
    }

    public static List<String> getAllProdEnvironments() throws Exception {
        List<String> prodEnvironments = new ArrayList<>();
        List<String> allEnvironments = getAllEnvironments();
        allEnvironments.forEach(e->{
            if(e.startsWith("aiappsociprod")){
                prodEnvironments.add(e);
            }
        });

        return prodEnvironments;
    }

    public static List<String> getAllEnvironments() throws Exception{
        String envHome=System.getenv("ENV_ACCESS_HOME");
        if (envHome == null) {
            throw new Exception("Please set ENV_ACCESS_HOME environment variable pointing to environment-access");

        }
        File envHomeFolder = new File(envHome);
        List<String > devRegions = Files.list(Paths.get(envHome)).
                filter(p -> p.toFile().getName().contains("aiapps0")).
                filter(p -> new File(p.toUri()).isDirectory()).
                map(p->p.toFile().getName()).
                collect(Collectors.toList());

        List<String > prodRegions = Files.list(Paths.get(envHome)).
                filter(p -> p.toFile().getName().contains("aiappsociprod")).
                filter(p -> new File(p.toUri()).isDirectory()).
                map(p->p.toFile().getName()).
                collect(Collectors.toList());

         devRegions.addAll(prodRegions);
        return devRegions;

    }

 /*   private Pod getPodFromLine(String line) {
        Pod pod = new Pod();
        String[] split = line.split("\\s+");
        pod.setName(split[0]);
        pod.setReady(split[1]);
        pod.setStatus(split[2]);
        pod.setRestarts(split[3]);
        pod.setAge(split[4]);
        return pod;

    }*/

 /*   public List<Pod> getPods(String nameSpace) throws Exception {
        List<Pod> pods = new ArrayList<>();
        String command="kubectl get pods -n "+nameSpace;
        String output = CommandExecutor.executeCommand(command,getEnvironmentVars());
        if (output == null || output.isEmpty()) {
            System.out.println("Couldn't get recommendation pod");
            throw new Exception("Couldn't get recommendation pod");
        }

        String[] lines = output.split("\n");
        for (int i = 1; i < lines.length; i++) {
            pods.add(getPodFromLine(lines[i]));
        }
        return pods;
    }*/

    public Environment(String name,String plane) {
        this.name=name.toUpperCase();
        if(this.name.startsWith("aiapps0")||this.name.startsWith("DEV")){
            environmentType =type.DEV;
            envPrefix="aiapps0";
        }else {
            environmentType=type.PROD;
            envPrefix = "aiappsociprod";
        }
        this.plane=plane;
    }

    public Environment(String name) {
        this(name, "cp");
    }
    public Environment(){};

    public Map<String ,String > getDefaultEnvironmentVars() {
        Map<String, String> envs = new HashMap<>();
        System.out.println("\"NO_PROXY\"="+System.getenv("NO_PROXY"));

        envs.put("LANG","en_US.UTF-8");
        envs.put("HTTP_PROXY","www-proxy-brmdc.us.oracle.com:80");
        envs.put("HTTPS_PROXY","www-proxy-brmdc.us.oracle.com:80");
//        envs.put("NO_PROXY","localhost,127.0.0.1,.oracle.com,.oraclecorp.com,.oc9qadev.com,files.responsys.net,.aiapps.org,kubernetes.docker.internal");
        envs.put("NO_PROXY","localhost,127.0.0.1,.oracle.com,.oraclecorp.com,.oc9qadev.com,files.responsys.net,.aiapps.org,kubernetes.docker.internal,192.168.205.3,,138.3.64.0/18,144.25.0.0/17");
//        envs.put("NO_PROXY",System.getenv("NO_PROXY"));
        envs.put("LC_ALL","en_US.UTF-8");
        envs.put("file.encoding","UTF-8");

        return envs;
    }

    public Map<String ,String > getEnvironmentVars() {
        Map<String, String> envs = new HashMap<>();
        try {
            envs.put("KUBECONFIG", getKubeconfig());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        envs.put("LANG","en_US.UTF-8");
        envs.put("HTTP_PROXY","www-proxy-brmdc.us.oracle.com:80");
        envs.put("HTTPS_PROXY","www-proxy-brmdc.us.oracle.com:80");
        envs.put("NO_PROXY",System.getenv("NO_PROXY"));
//        envs.put("NO_PROXY","localhost,127.0.0.1,.oracle.com,.oraclecorp.com,.oc9qadev.com,files.responsys.net,.aiapps.org,kubernetes.docker.internal");
        envs.put("LC_ALL","en_US.UTF-8");
        envs.put("file.encoding","UTF-8");
        
        return envs;
    }

    public String getKubeconfig() throws Exception{
        String kubeConfig = null;
        String envAccessHome = System.getenv("ENV_ACCESS_HOME");
        if (envAccessHome == null) {
            throw new Exception("ENV_ACCESS_HOME environment not Set.Please clone environment-access repo");
        }
        kubeConfig=envAccessHome+ File.separator+name+File.separator+plane+"-kubeconfig";
        return kubeConfig;

    }
}
