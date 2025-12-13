import styles from "./Button.module.css";

interface Props {
    text: String;
    handleClick: React.MouseEventHandler<HTMLButtonElement>;
}

export const Button = ({ text, handleClick }: Props) => {
    return (
        <button 
            onClick={handleClick} 
            className={styles.button}
        >
            {text}
        </button>
    );
}