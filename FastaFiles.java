/**
 *
 * @author Ash
 */
package fastafiles;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class FastaFiles extends Configured implements Tool {
private static final Log LOG = LogFactory.getLog(FastaFiles.class);

   static public class FastaFilesMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
      final private static LongWritable ONE = new LongWritable(1);
      private Text tokenValue = new Text();

      @Override
      protected void map(LongWritable offset, Text text, Context context) throws IOException, InterruptedException {
         String line = text.toString();
         String test = "";
         boolean flag = false;
         Configuration conf = context.getConfiguration();
         String kmer = conf.get("kmer");
         //LOG.info(">>>>>>>>" + kmer);
         tokenValue.set("No. of reads");
	 context.write(tokenValue, ONE);
         char[] characters = line.toCharArray();
         for(int i =0; i<characters.length; i++)
         {
             test = "";
             for(int j= i; j<(i+Integer.parseInt(kmer)); j++)
             {
                 if((i+Integer.parseInt(kmer))<=characters.length)
                 {
                     test = test + Character.toString(characters[j]);
                 }
                 else
                 {
                     break;
                 }
             }
             if(!"".equals(test))
             {
                flag = false;
                char[] chartrial = test.toCharArray();
                for(int x=0; x<chartrial.length; x++)
                {
                    if(Character.isUpperCase(chartrial[x]))
                    {
                       if(chartrial[x] == 'A' ||  chartrial[x] == 'C' || chartrial[x] == 'G' || chartrial[x] == 'T')
                        {
                            
                            flag = true;
                        }
                       else
                       {
                          flag = false;
                        break; 
                       }
                    }
                    else
                    {
                        flag = false;
                        break;
                    }
                }
                if(flag == true)
                {
                    tokenValue.set(test);
                    context.write(tokenValue, ONE);  
                }
               
             }         
         }
      }
   }

   static public class FastaFilesReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
      private LongWritable total = new LongWritable();

      @Override
      protected void reduce(Text token, Iterable<LongWritable> counts, Context context)
            throws IOException, InterruptedException {
         long n = 0;
         for (LongWritable count : counts)
            n += count.get();
         total.set(n);
         context.write(token, total);
      }
   }

  @SuppressWarnings("deprecation")
   public int run(String[] args) throws Exception {
      Configuration configuration = getConf();
      configuration.set("kmer", args[2]);
      Job job = new Job(configuration, "FastaFiles");
      job.setJarByClass(FastaFiles.class);

      job.setMapperClass(FastaFilesMapper.class);
      job.setCombinerClass(FastaFilesReducer.class);
      job.setReducerClass(FastaFilesReducer.class);
      

      job.setInputFormatClass(FastaFileInputFormat.class);
      job.setOutputFormatClass(TextOutputFormat.class);
      
      FileInputFormat.setInputPaths(job, new Path(args[0]));
      FileOutputFormat.setOutputPath(job, new Path(args[1]));
      
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(LongWritable.class);

      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(LongWritable.class);

      return job.waitForCompletion(true) ? 0 : -1;
   }

   public static void main(String[] args) throws Exception {
      System.exit(ToolRunner.run(new FastaFiles(), args));
   }
}