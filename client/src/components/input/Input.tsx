import styles from "./Input.module.css";

interface Props {
    label: String;
    type: String;
    value: string;
    setValue: React.Dispatch<React.SetStateAction<string>>;
}

export const Input = ({ label, type, value, setValue}: Props) => {
    return (
        <div className={styles.inputContainer}>
            <label 
                className={styles.label}
            >
                {label}
            </label>

            <input 
                className={styles.input} 
                type={`${type}`} 
                value={value} 
                onChange={e => setValue(e.target.value)} 
            />
        </div>
    );
}