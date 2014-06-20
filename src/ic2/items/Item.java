package ic2.items;

public class Item {

	String text;
	int serial_num;
	
	long list_id;

	int status;
	
	long db_id;
	long created_at;
	long modified_at;
	
	public Item(String text, int serial_num, long list_id) {

		this.text = text;
		this.serial_num = serial_num;
		this.list_id = list_id;

	}//public Items(String text, int serial_num, long list_id)

	public Item(String text, int serial_num, long list_id, 
				long db_id, long created_at, long modified_at) {

		this.text = text;
		this.serial_num = serial_num;
		this.list_id = list_id;
		
		this.db_id = db_id;
		this.created_at = created_at;
		this.modified_at = modified_at;

	}//public Items(String text, int serial_num, long list_id)

	public Item(
			String text, int serial_num, 
			long list_id, int status,
			
			long db_id, long created_at, long modified_at) {

		this.text = text;
		this.serial_num = serial_num;
		this.list_id = list_id;
		this.status = status;
		
		this.db_id = db_id;
		this.created_at = created_at;
		this.modified_at = modified_at;
	
	}//public Items(String text, int serial_num, long list_id)

	
	public Item(Builder builder) {
		// TODO Auto-generated constructor stub
		
		this.text	= builder.text;
		this.serial_num	= builder.serial_num;
		
		this.list_id	= builder.list_id;

		this.status	= builder.status;
		
		this.db_id	= builder.db_id;
		this.created_at	= builder.created_at;
		this.modified_at	= builder.modified_at;
		
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getSerial_num() {
		return serial_num;
	}

	public void setSerial_num(int serial_num) {
		this.serial_num = serial_num;
	}

	public long getList_id() {
		return list_id;
	}

	public void setList_id(long list_id) {
		this.list_id = list_id;
	}

	public long getDb_id() {
		return db_id;
	}

	public void setDb_id(long db_id) {
		this.db_id = db_id;
	}

	public long getModified_at() {
		return modified_at;
	}

	public void setModified_at(long modified_at) {
		this.modified_at = modified_at;
	}

	public long getCreated_at() {
		return created_at;
	}


	public static class Builder {
		
		String text;
		int serial_num;
		
		long list_id;

		int status;
		
		long db_id;
		long created_at;
		long modified_at;
		
		public Item build() {
			
			return new Item(this);
			
		}
		
		public Builder setText(String text) {
			this.text = text; return this;
		}
		public Builder setSerial_num(int serial_num) {
			this.serial_num = serial_num; return this;
		}
		public Builder setList_id(long list_id) {
			this.list_id = list_id; return this;
		}
		public Builder setStatus(int status) {
			this.status = status; return this;
		}
		public Builder setDb_id(long db_id) {
			this.db_id = db_id; return this;
		}
		public Builder setCreated_at(long created_at) {
			this.created_at = created_at; return this;
		}
		public Builder setModified_at(long modified_at) {
			this.modified_at = modified_at; return this;
		}

		
	}
}//public class Item
