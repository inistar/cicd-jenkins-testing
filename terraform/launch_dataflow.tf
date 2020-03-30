
provider "google" {
  credentials = file("/Users/inis/KEYS/myspringml2-a36f8d343840.json")
  project     = "myspringml2"
  region      = "us-central1"
}

resource "google_dataflow_job" "big_data_job" {
  name              = "terraform-trigger"
  template_gcs_path = "gs://jdbc-testing/GenerateSequenceToGCS/template"
  temp_gcs_location = "gs://jdbc-testing/GenerateSequenceToGCS/tmp_dir"
  zone              = "us-central1-a"
}